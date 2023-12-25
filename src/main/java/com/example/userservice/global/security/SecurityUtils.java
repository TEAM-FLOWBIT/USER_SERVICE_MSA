package com.example.userservice.global.security;

import com.example.userservice.global.exception.error.NotFoundAccountException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityUtils {

    private static SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");

    private static List<SimpleGrantedAuthority> notUserAuthority = List.of(admin);

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundAccountException("유저를 찾을 수 없습니다");
        }

        if (authentication.isAuthenticated()
//                &&
//                !CollectionUtils.containsAny(
//                authentication.getAuthorities(), notUserAuthority
                ) {
            return authentication.getName();
        }
        // admin 유저일시 유저 아이디 0 반환
        return "admin@naver.com";
    }
}