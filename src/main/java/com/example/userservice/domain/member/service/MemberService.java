package com.example.userservice.domain.member.service;

import com.example.userservice.domain.member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.global.common.CommonResDto;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    CommonResDto<CreateMemberResponseDto> createMember(MultipartFile multipartFile,SignUpRequestDto signUpRequestDto) throws IOException, FileUploadException;

}
