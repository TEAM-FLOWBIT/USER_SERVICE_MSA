package com.example.userservice.domain.auth.jwt;

import com.example.userservice.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class MemberDetails implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("MemberDetails.getAuthorities");
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//
//        // Assuming getMemberRole() returns a String representing the role
//        MemberRole memberRole = member.getMemberRole();
//
//        // Add the actual role as a GrantedAuthority
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(memberRole.toString());
//        authorities.add(authority);

        return null;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }


    @Override
    public String getUsername() {
        return member.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getState();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO
        return true;
    }
}
