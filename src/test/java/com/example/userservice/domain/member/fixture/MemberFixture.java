package com.example.userservice.domain.member.fixture;

import com.example.userservice.domain.member.dto.request.SignUpRequestDto;

public class MemberFixture {

    public static SignUpRequestDto createMember(String userId){
        return  SignUpRequestDto.builder()
                .phone("010-1234-1234")
                .name("김민우1234")
                .password("anstn1234@")
                .userId(userId)
                .nickname("민우닉네임")
                .profile("flowbit-default-profile.png")
                .build();

    }


}
