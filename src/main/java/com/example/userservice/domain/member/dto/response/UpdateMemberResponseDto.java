package com.example.userservice.domain.member.dto.response;

import com.example.userservice.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberResponseDto {
    private Long memberId;
    private String name;
    private String phone;
    private String profile;

    @Builder
    public UpdateMemberResponseDto(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.profile = member.getProfile();
    }
}
