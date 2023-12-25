package com.example.userservice.domain.member.controller;

import com.example.userservice.domain.member.dto.request.DeleteMemberRequestDto;
import com.example.userservice.domain.member.dto.request.MemberLoginRequestDto;
import com.example.userservice.domain.member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.member.dto.response.MemberLoginResponseDto;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.fixture.MemberFixture;
import com.example.userservice.domain.member.service.MemberService;
import com.example.userservice.global.config.redis.util.EmailRedisUtil;
import com.example.userservice.util.ControllerTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;
import static org.apache.commons.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends ControllerTestSupport {

    @Autowired
    EmailRedisUtil emailRedisUtil;
    @Autowired
    MemberService memberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        emailRedisUtil.deleteData("kbsserver@naver.com");
    }

    @DisplayName("유저는 회원가입을 할 수 있다.")
    @Test
    void test() throws Exception {
        // given

        String url = "/api/v1/member";

        SignUpRequestDto signUpRequestDto = MemberFixture.signUpRequestDto;


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

        SignUpRequestDto signUpRequestDto = MemberFixture.signUpRequestDto;


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
    @WithMockUser(username = "kbsserver@naver.com")
    void deleteMember() throws Exception {
        // given

        String url = "/api/v1/member";

        SignUpRequestDto signUpRequestDto = MemberFixture.signUpRequestDto;


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


        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .phone("010-1234-1234")
                .name("김민우1234")
                .password("anstn1234@")
                .userId("kbsserver2@naver.com")
                .nickname("민우닉네임")
                .profile("flowbit-default-profile.png")
                .build();

        emailRedisUtil.setListData(signUpRequestDto.getUserId(),0,"signupVerifySuccess",60*20L);

        memberService.createMember(null,signUpRequestDto);

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .userId("kbsserver2@naver.com")
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










}