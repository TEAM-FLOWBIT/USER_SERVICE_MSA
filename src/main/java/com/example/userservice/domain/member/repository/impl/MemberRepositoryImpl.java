package com.example.userservice.domain.member.repository.impl;

import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.repository.MemberQuerydslRepository;
import com.example.userservice.global.config.Querydsl4RepositorySupport;

import java.util.Optional;

import static com.example.userservice.domain.member.entity.QMember.member;


public class MemberRepositoryImpl extends Querydsl4RepositorySupport implements MemberQuerydslRepository {


    public MemberRepositoryImpl() {
        super(Member.class);
    }

    public Optional<Member> findMemberByUserId(String userId) {
        return Optional.ofNullable(select(member)
                .from(member)
                .where(member.userId.eq(userId).and(member.state.eq(true))).
                fetchOne());
    }
}
