package com.example.userservice.domain.auth.jwt;

import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberService memberService, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthorizationFilter.doFilterInternal");
        String path = request.getServletPath();
        System.out.println("path = " + path);
        String header = jwtProvider.getHeader(request);
        if (header == null) {
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        username = jwtProvider.getUserId(request);

        if (username != null) {
            log.info("username = " + username);
            Member member = memberService.findMemberByUserId(username);
            MemberDetails memberDetails = new MemberDetails(member);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}