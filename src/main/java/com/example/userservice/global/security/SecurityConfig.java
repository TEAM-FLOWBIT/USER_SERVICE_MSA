package com.example.userservice.global.security;


import com.example.userservice.domain.auth.jwt.JwtAuthenticationFilter;
import com.example.userservice.domain.auth.jwt.JwtAuthorizationFilter;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.service.RefreshTokenService;
import com.example.userservice.domain.member.service.MemberService;
import com.example.userservice.global.exception.GlobalExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final GlobalExceptionHandlerFilter globalExceptionHandlerFilter;



    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling()
                .and()
                .formLogin().disable() // 초기 로그인화면 자동생성 안함
                .csrf().disable() // rest api 에서 csrf 방어 필요 없음
                .cors().configurationSource(corsConfigurationSource()).and() // cors
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// 세션 생성 안함 , response 에 setcookie jsessionId= ... 안함
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), refreshTokenService, jwtProvider))
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), memberService, jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/member/renew-access-token").permitAll()
                .anyRequest().authenticated();
        httpSecurity.addFilterAfter(globalExceptionHandlerFilter,LogoutFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();


        configuration.addAllowedOrigin("https://frontserver.apps.sys.paas-ta-dev10.kr");
        configuration.addAllowedOriginPattern("*localhost*"); // A list of origins for which cross-origin requests are allowed. ex) http://localhost:8080
        configuration.addAllowedHeader("*"); // Set the HTTP methods to allow ,ex) "GET", "POST", "PUT";
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("*"); // 모든걸 허용함
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


//

}