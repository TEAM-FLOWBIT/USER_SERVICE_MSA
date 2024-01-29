package com.example.userservice.domain.auth.oauth;

import com.example.userservice.domain.auth.MemberRole;
import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.service.RefreshTokenService;
import com.example.userservice.domain.member.dto.response.MemberLoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${refer.host}")
    private String referHost;

    /*Todo 추가적인 정보를 받을 것인지에 대해 다음정기회의 때 이야기해보기*/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 가서 회원가입 마무리 해야함 (추가정보 입력 후..)
//        if(oAuth2Member.getMemberRole() == MemberRole.GUEST) {
//            throw new ApplicationException(ApplicationErrorType.GUEST_USER);
//        } else {
            loginSuccess(response,authentication, oAuth2Member,referHost); // 로그인에 성공한 경우 access, refresh 토큰 생성
//        }
    }
    private void loginSuccess(HttpServletResponse response,Authentication authentication, CustomOAuth2Member oAuth2Member,String domain) {
        log.info("OAuth2LoginSuccessHandler.loginSuccess");


        String accessToken = jwtProvider.generateAccessToken(oAuth2Member.getUsername(),authentication);
        String refreshToken = jwtProvider.generateRefreshToken(oAuth2Member.getUsername(),authentication);

        refreshTokenService.setRefreshToken(oAuth2Member.getUsername(), refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        CookieUtil.addCookie(response, "refreshToken", refreshToken, jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);
        try {
            // token body comment
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            MemberLoginResponseDto.builder()
                                    .accessToken(accessToken)
                                    .build()
                    )
            );
            response.sendRedirect(domain+"oauth2/redirect?accessToken=" + accessToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
