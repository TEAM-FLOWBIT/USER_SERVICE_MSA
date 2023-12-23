package com.example.userservice.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberResponseDto {
    private String userId;
    private String name;
    private String phone;
    private String nickname;


    @Builder
    public CreateMemberResponseDto(String userId, String name, String phone, String nickname) {
        this.userId=userId;
        this.name = name;
        this.phone = phone;
        this.nickname=nickname;
    }
}
