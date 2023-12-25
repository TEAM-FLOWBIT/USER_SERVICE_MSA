package com.example.userservice.domain.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteMemberRequestDto {

    @NotEmpty(message = "비밀번호 설정은 필수입니다.")
    private String password;
}