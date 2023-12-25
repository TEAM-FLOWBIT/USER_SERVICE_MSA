package com.example.userservice.domain.member.controller;


import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.service.RefreshTokenService;
import com.example.userservice.domain.member.dto.request.DeleteMemberRequestDto;
import com.example.userservice.domain.member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.member.dto.response.MemberRenewAccessTokenResponseDto;
import com.example.userservice.domain.member.service.MemberService;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.exception.error.InvalidTokenException;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberController {
    private final Environment env;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("")
    public ResponseEntity<CommonResDto<CreateMemberResponseDto>> createMember(

            @Valid @ModelAttribute SignUpRequestDto signUpRequestDto) throws IOException, FileUploadException {

        log.info("회원가입 진행 중");
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(signUpRequestDto.getProfileFile(),signUpRequestDto));
    }

    @GetMapping("/renew-access-token")
    public ResponseEntity renewAccessTokenForGetMapping(
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        String cookieRefreshToken = CookieUtil.getRefreshTokenCookie(request);
        log.info("before renew refreshToken : "+cookieRefreshToken);
        try {
            Authentication authentication = jwtProvider.validateAndSetAuthentication(cookieRefreshToken);

            String accessToken = memberService.renewAccessToken(cookieRefreshToken, authentication);
            String refreshToken = jwtProvider.renewRefreshToken(authentication);

            refreshTokenService.setRefreshToken(authentication.getName(), refreshToken);
            CookieUtil.addCookie(response, "refreshToken", refreshToken, jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);

            return ResponseEntity.ok(MemberRenewAccessTokenResponseDto.builder()
                    .accessToken(accessToken)
                    .build());
        } catch (InvalidTokenException invalidTokenException) {
            throw new InvalidTokenException("토큰이 유효하지 않습니다");
        }
    }
    @PostMapping("/renew-access-token")
    public ResponseEntity renewAccessTokenForPostMapping(
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        String cookieRefreshToken = CookieUtil.getRefreshTokenCookie(request);
        log.info("before renew refreshToken : "+cookieRefreshToken);
        try {
            Authentication authentication = jwtProvider.validateAndSetAuthentication(cookieRefreshToken);

            String accessToken = memberService.renewAccessToken(cookieRefreshToken, authentication);
            String refreshToken = jwtProvider.renewRefreshToken(authentication);

            refreshTokenService.setRefreshToken(authentication.getName(), refreshToken);
            CookieUtil.addCookie(response, "refreshToken", refreshToken, jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);

            return ResponseEntity.ok(MemberRenewAccessTokenResponseDto.builder()
                    .accessToken(accessToken)
                    .build());
        } catch (InvalidTokenException invalidTokenException) {
            throw new InvalidTokenException("토큰이 유효하지 않습니다");
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteMember(@RequestBody DeleteMemberRequestDto deleteMemberRequestDto, Principal principal) {

        log.info("회원삭제 진행 중");
        memberService.deleteMember(deleteMemberRequestDto,principal.getName());
        return new ResponseEntity<>(new CommonResDto<>(1,"회원삭제완료",null), HttpStatus.OK);
    }





    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port) = " + env.getProperty("server.port")
                + ", token secret  = " + env.getProperty("token.secret")
                + ", token expiration time= " + env.getProperty("token.expiration_time")
        );
    }

}