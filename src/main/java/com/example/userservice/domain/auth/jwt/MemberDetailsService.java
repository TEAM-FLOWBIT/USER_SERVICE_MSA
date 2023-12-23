package com.example.userservice.domain.auth.jwt;

import com.example.userservice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            log.info("MemberDetailsService.loadUserByUsername");
            return new MemberDetails(memberRepository.findMemberByUserId(username).orElseThrow(() -> new UsernameNotFoundException("no user with username: " + username)));
        }

}
