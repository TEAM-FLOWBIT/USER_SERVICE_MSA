package com.example.userservice.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginResponseDto {

    String accessToken;

    @Builder
    public MemberLoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
