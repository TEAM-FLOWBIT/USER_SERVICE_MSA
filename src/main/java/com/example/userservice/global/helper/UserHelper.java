package com.example.userservice.global.helper;

import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.repository.MemberRepository;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import com.example.userservice.global.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final MemberRepository memberRepository;

    public String getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Member getMember(){
        return memberRepository.findByUserId(getCurrentUserId()).orElseThrow(NotFoundAccountException::new);
    }


}