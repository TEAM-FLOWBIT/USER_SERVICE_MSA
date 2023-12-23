package com.example.userservice.domain.member.service.impl;

import com.example.userservice.domain.auth.entity.RefreshToken;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.repository.RefreshTokenRepository;
import com.example.userservice.domain.member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.repository.MemberRepository;
import com.example.userservice.domain.member.repository.dao.MemberDao;
import com.example.userservice.domain.member.service.MemberService;
import com.example.userservice.global.aws.AwsS3Service;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.config.redis.util.EmailRedisUtil;
import com.example.userservice.global.exception.error.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailRedisUtil emailRedisUtil;
    private final AwsS3Service awsS3Service;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;






    @Transactional
    public CommonResDto<CreateMemberResponseDto> createMember(MultipartFile multipartFile,SignUpRequestDto signUpRequestDto) throws FileUploadException {

        Member savedMember=null;
        //중복체크검사
        signupVaidate(signUpRequestDto);
        // 이메일인증 여부
        emailVerifyCheck(signUpRequestDto);
        ProfileUploadForcreateMember(multipartFile, signUpRequestDto);
        //회원가입 및 db save
        savedMember = registerMember(signUpRequestDto);
        return new CommonResDto<>(1,"회원가입성공",CreateMemberResponseDto.builder()
                .userId(savedMember.getUserId())
                .phone(savedMember.getPhone())
                .name(savedMember.getName())
                .nickname(savedMember.getNickname())
                .build());
    }

    @Override
    @Transactional
    public String renewAccessToken(String refreshToken,Authentication authentication) {
        log.info("refreshToken = " + refreshToken);
        if (!jwtProvider.verifyToken(refreshToken)) {
            throw new InvalidTokenException();
        }
        String username = jwtProvider.getUsernameFromToken(refreshToken);
        RefreshToken refreshTokenFound = refreshTokenRepository.findById(username).orElseThrow(NotFoundAccountException::new);
        if (!refreshTokenFound.getToken().equals(refreshToken)) {
            throw new RuntimeException("not matching refreshToken");
        }

        return jwtProvider.generateAccessToken(username,authentication);
    }



    public Member findMemberByUserId(String userId) {
        return memberDao.findMemberByUserId(userId);
    }


    private void ProfileUploadForcreateMember(MultipartFile multipartFile, SignUpRequestDto signUpRequestDto) throws FileUploadException {
        try {
            if (multipartFile !=null) {
                log.info("파일업로드 중");
                // db 저장 후 AWS S3 업로드
                String filename = awsS3Service.upload(multipartFile.getOriginalFilename(), multipartFile, signUpRequestDto.getUserId());
                // 회원가입DTO에 이미지파일이름 저장
                signUpRequestDto.setProfile(filename);
            }else{
                signUpRequestDto.setProfile("flowbit-default-profile.png");
            }
        }catch (IOException e) {
            throw new FileUploadException("파일 업로드에 실패하였습니다");
        }
    }

    private void emailVerifyCheck(SignUpRequestDto signUpRequestDto) {
        log.info("회원가입 이메일 검증 중");

        List<String> data = emailRedisUtil.getData(signUpRequestDto.getUserId());
        if (data.isEmpty()){
            throw new EmailNotValidException();
        }
        String[] splitInfo = data.get(0).split("\\|");
        String redisVerifyPurpose = splitInfo[1];
        if(!Objects.equals(redisVerifyPurpose, "signupVerifySuccess")){
            throw new EmailNotValidException();
        }
    }


    /**
     * 회원등록
     */
    private Member registerMember(SignUpRequestDto signUpRequestDto){
        String password=signUpRequestDto.getPassword();
        signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(password));
        Member member = signUpRequestDto.toEntity();
        return memberDao.insertMember(member);
    }


    /**
     * 회원유효성검증
     */
    private void signupVaidate(SignUpRequestDto signUpRequestDto){

        String userId = signUpRequestDto.getUserId();
        System.out.println("회원 유효성검증");
        if (memberDao.duplicateMemberCheck(userId).isPresent()) {
            throw new DuplicateAccountException("아이디 중복");
        }
    }


}
