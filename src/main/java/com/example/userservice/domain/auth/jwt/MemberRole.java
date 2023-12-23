package com.example.userservice.domain.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ROOT ("ROLE_ROOT") ,// (대표)계정
    ADMIN("ROLE_ADMIN"), // 파트장 및 교육팀장
    STAFF ("ROLE_STAFF"), // 멋사 운영진
    LION ("ROLE_LION"), // 아기사자
    APPLY("ROLE_APPLY");// 지원자
    private final String key;
}
