package com.example.userservice.domain.member.controller;

import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.member.dto.request.DeleteMemberRequestDto;
import com.example.userservice.domain.member.dto.request.MemberLoginRequestDto;
import com.example.userservice.domain.member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.member.dto.request.UpdateMemberRequestDto;
import com.example.userservice.domain.member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.member.dto.response.MemberLoginResponseDto;
import com.example.userservice.domain.member.dto.response.UpdateMemberResponseDto;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.fixture.MemberFixture;
import com.example.userservice.domain.member.service.MemberService;
import com.example.userservice.global.config.redis.util.EmailRedisUtil;
import com.example.userservice.util.ControllerTestSupport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.apache.catalina.connector.Response;
import org.apache.commons.fileupload.FileUploadException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.apache.commons.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends ControllerTestSupport {

    @Autowired
    EmailRedisUtil emailRedisUtil;
    @Autowired
    MemberService memberService;
    @Autowired
    JwtProvider jwtProvider;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();

    }

    @DisplayName("유저는 회원가입을 할 수 있다.")
    @Test
    void memberSignup() throws Exception {
        // given

        String url = "/api/v1/member";

        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver@naver.com");


        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);

        //when //then
        mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("userId", signUpRequestDto.getUserId())
                        .param("password", signUpRequestDto.getPassword())
                        .param("phone",signUpRequestDto.getPhone())
                        .param("name",signUpRequestDto.getName())
                        .param("nickname",signUpRequestDto.getNickname()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("이미 가입된 id는 회원가입을 할 수 없다.")
    @Test
    void createMemberWithSameID() throws Exception {
        // Given
        Member member = Member.builder()
                .phone("010-1234-1234")
                .name("김민우1234")
                .password("anstn1234@")
                .userId("kbsserver@naver.com")
                .nickname("민우닉네임")
                .profile("flowbit-default-profile.png")
                .state(true)
                .build();

        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver@naver.com");



        String url = "/api/v1/member";

        memberRepository.saveAndFlush(member);

        mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("userId", signUpRequestDto.getUserId())
                        .param("password", signUpRequestDto.getPassword())
                        .param("phone", signUpRequestDto.getPhone())
                        .param("name", signUpRequestDto.getName())
                        .param("nickname", signUpRequestDto.getNickname())
                ).andDo(print())
                .andExpect(status().isConflict())
                .andReturn();

    }

    @DisplayName("유저는 회원탈퇴를 할 수 있다.")
    @Test
    @WithMockUser(username = "kbsserver2@naver.com")
    void deleteMember() throws Exception {
        // given

        String url = "/api/v1/member";

        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver2@naver.com");


        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);

        memberService.createMember(null,signUpRequestDto);

        DeleteMemberRequestDto deleteMemberRequestDto =DeleteMemberRequestDto.builder()
                .password("anstn1234@")
                .build();


        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(deleteMemberRequestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }


    @DisplayName("회원가입한 사용자는 로그인을 할 수 있다.")
    @Test
    void loginMember() throws Exception {
        //given
        String url = "/api/v1/member/login";

        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver3@naver.com");

        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);

        memberService.createMember(null,signUpRequestDto);

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .userId("kbsserver3@naver.com")
                .password("anstn1234@")
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String result = mvcResult.getResponse().getContentAsString();
        MemberLoginResponseDto memberLoginResponseDto = objectMapper.readValue(result, MemberLoginResponseDto.class);
        assertThat(memberLoginResponseDto).isNotNull();
        assertThat(memberLoginResponseDto.getAccessToken()).isNotNull();
    }

    @DisplayName("유저의 정보를 조회할 수 있다.")
    @WithMockUser(username = "kbsserver4@naver.com")
    @Test
    void readUserInfo() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver4@naver.com");
        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);
        memberService.createMember(null,signUpRequestDto);

        String url="/api/v1/member/info";
        //when
        MvcResult mvcResult = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();



        //then

        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");

        String nickname = resultData.get("nickname").asText();
        Long id = resultData.get("id").asLong();
        String email = resultData.get("email").asText();
        String profile = resultData.get("profile").asText();
        String name = resultData.get("name").asText();

        assertThat(id).isNotNull();
        assertThat(email).isEqualTo("kbsserver4@naver.com");
        assertThat(name).isEqualTo("김민우1234");
        assertThat(profile).isEqualTo("flowbit-default-profile.png");
        assertThat(nickname).isEqualTo("민우닉네임");
    }

    @DisplayName("사용자는 비밀번호의 유효성이 일치하다면 회원정보를 수정할 수 있다.")
    @Test
    @WithMockUser(username = "kbsserver5@naver.com")
    void passwordValidationPassThenUpdateMemberInfo() throws Exception {

        //given
        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver5@naver.com");
        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);
        memberService.createMember(null,signUpRequestDto);


        UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .name("김민우수정")
                .password("anstn1234@")
                .phoneNumber("010-1234-1234")
                .build();

        String url="/api/v1/member/profile-update";
        //when
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("password", updateMemberRequestDto.getPassword())
                        .param("phoneNumber", updateMemberRequestDto.getPhoneNumber())
                        .param("name", updateMemberRequestDto.getName())
                        .characterEncoding(StandardCharsets.UTF_8)

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then

        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");

        String phone = resultData.get("phone").asText();
        String name = resultData.get("name").asText();

        assertThat(name).isEqualTo("김민우수정");
        assertThat(phone).isEqualTo("010-1234-1234");
    }

    @DisplayName("사용자는 비밀번호의 유효성이 일치하지않다면 회원정보를 수정할 수 없다.")
    @Test
    @WithMockUser(username = "kbsserver6@naver.com")
    void passwordValidationFailThenUpdateMemberInfo() throws Exception {

        //given
        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver6@naver.com");
        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);
        memberService.createMember(null,signUpRequestDto);


        UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .name("김민우수정")
                .password("anstn12")
                .phoneNumber("010-1234-1234")
                .build();

        String url="/api/v1/member/profile-update";
        //when
        mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("password", updateMemberRequestDto.getPassword())
                        .param("phoneNumber", updateMemberRequestDto.getPhoneNumber())
                        .param("name", updateMemberRequestDto.getName())
                        .characterEncoding(StandardCharsets.UTF_8)

                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @DisplayName("로그인을 한 사용자가 로그아웃을 하게되면 쿠키값을 지워서 반환해준다.")
    @Test
    void memberLogoutThenDeleteCookie() throws Exception {
        //given
//        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver6@naver.com");
//        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);
//        memberService.createMember(null,signUpRequestDto);


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies();
        String logoutUrl="/api/v1/member/logout";
        //when


        ResultActions resultActions = mockMvc.perform(post(logoutUrl)
                        .cookie(new Cookie("refreshToken", "refreshToken"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(
                        result -> {
                            assertThat(Arrays.stream(result.getResponse().getCookies()).map(cookie -> cookie.getName().equals("refreshToken") && cookie.getValue().equals(""))).isNotNull();
                        }
                );

    }

}