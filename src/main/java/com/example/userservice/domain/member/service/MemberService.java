package com.example.userservice.domain.member.service;

import com.example.userservice.domain.member.dto.request.DeleteMemberRequestDto;
import com.example.userservice.domain.member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.member.dto.request.UpdateMemberRequestDto;
import com.example.userservice.domain.member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.member.dto.response.MemberInfoByMemberIdResponseDto;
import com.example.userservice.domain.member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.member.dto.response.UpdateMemberResponseDto;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.global.common.CommonResDto;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    CommonResDto<CreateMemberResponseDto> createMember(MultipartFile multipartFile,SignUpRequestDto signUpRequestDto) throws IOException, FileUploadException;
    MemberInfoResponseDto getMemberInfo(String name);
    String renewAccessToken(String refreshToken, Authentication authentication);
    Member findMemberByUserId(String username);
    void deleteMember(DeleteMemberRequestDto deleteMemberRequestDto,String name);

    void memberLogout(String username);

    UpdateMemberResponseDto updateMember(String memberId, UpdateMemberRequestDto updateMemberRequestDto, MultipartFile multipartFile) throws IOException, FileUploadException;

    MemberInfoByMemberIdResponseDto getMemberInfoBymemberId(Long memberId);
}
