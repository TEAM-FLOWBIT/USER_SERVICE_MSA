package com.example.userservice.domain.member.repository;

import com.example.userservice.domain.auth.ProviderType;
import com.example.userservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long>,MemberQuerydslRepository {
    Optional<Member> findByUserId(String userId);

    Optional<Member> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
}