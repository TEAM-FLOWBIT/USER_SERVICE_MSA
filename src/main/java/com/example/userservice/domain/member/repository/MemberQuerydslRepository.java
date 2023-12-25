package com.example.userservice.domain.member.repository;

import com.example.userservice.domain.member.entity.Member;

import java.util.Optional;

public interface MemberQuerydslRepository {
    Optional<Member> findMemberByUserId(String userId);
}
