package com.example.userservice.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String nickname;
    private String profile;

    @Builder
    public MemberInfoResponseDto(Long id, String name, String phone, String email, String nickname,String profile) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
        this.profile=profile;
    }
}
