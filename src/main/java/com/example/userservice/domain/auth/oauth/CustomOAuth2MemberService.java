package com.example.userservice.domain.auth.oauth;

import com.example.userservice.domain.auth.ProviderType;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private static final String KAKAO = "kakao";

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2MemberService.loadUser");
        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 ProviderType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderType providerType = getProviderType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)
        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(providerType, userNameAttributeName, attributes);
        Member createdUser = getUser(extractAttributes, providerType); // getUser() 메소드로 Member 객체 생성 후 반환

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2Member(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getMemberRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getUserId(),
                createdUser.getMemberRole()
        );
    }
    private ProviderType getProviderType(String registrationId) {
        if(KAKAO.equals(registrationId)) {
            return ProviderType.KAKAO;
        }
        return ProviderType.GOOGLE;
    }

    /**
     * ProviderType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 saveUser()를 호출하여 회원을 저장한다.
     */

    private Member getUser(OAuthAttributes attributes, ProviderType providerType) {
        log.info("CustomOAuth2MemberService.getUser");
        Member findUser = memberRepository.findByProviderTypeAndProviderId(providerType,
                attributes.getOauth2MemberInfo().getId()).orElse(null);

        if(findUser == null) {
            return saveUser(attributes, providerType);
        }
        return findUser;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 Member 객체 생성 후 반환
     * 생성된 Member 객체를 DB에 저장 : providerType, socialId, email, role 값만 있는 상태
     */
    @Transactional
    private Member saveUser(OAuthAttributes attributes, ProviderType providerType) {
        log.info("CustomOAuth2MemberService.saveUser");
        Member createdMember = attributes.toEntity(providerType, attributes.getOauth2MemberInfo());
        log.info("createdMember = " + createdMember);
        return memberRepository.save(createdMember);
    }
}
