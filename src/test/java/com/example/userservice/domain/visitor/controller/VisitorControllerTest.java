package com.example.userservice.domain.visitor.controller;

import com.example.userservice.domain.member.dto.response.MemberLoginResponseDto;
import com.example.userservice.domain.visitor.fixture.VisitorLocalDateTime;
import com.example.userservice.domain.visitor.repository.VisitorRepository;
import com.example.userservice.domain.visitor.util.ViewCountUtil;
import com.example.userservice.global.config.redis.util.VisitorRedisUtil;
import com.example.userservice.util.ControllerTestSupport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VisitorControllerTest extends ControllerTestSupport {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private ViewCountUtil viewCountUtil;
    @Autowired
    private VisitorRedisUtil visitorRedisUtil;

    @AfterEach
    void tearDown() {
        int year = VisitorLocalDateTime.YEAR;
        int month = VisitorLocalDateTime.MONTH;
        int day = VisitorLocalDateTime.DAY;


        visitorRepository.deleteAllInBatch();
        visitorRedisUtil.deleteData("viewCount_Home_1"+year+month+day);
    }

    @DisplayName("메인 페이지에 접속하면 redis에서 조회수가 1회 증가한다.")
    @Test
    void increaseViewCount() throws Exception {
        //given


        int year = VisitorLocalDateTime.YEAR;
        int month = VisitorLocalDateTime.MONTH;
        int day = VisitorLocalDateTime.DAY;

        String mockIp = "192.168.123.123";
        //when
        mockMvc.perform(post("/api/v1/visitor")
                        .header("X-FORWARDED-FOR", mockIp)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        assertThat(viewCountUtil.getViewCount("viewCount_Home_1"+year+month+day)).isEqualTo(1);
    }

    @DisplayName("메인페이지에 접속하면 일일방문자 수를 조회할 수 있다.")
    @Test
    void enterMainPageThenReadTodayViewCount() throws Exception {
        //given
        String mockIp = "192.168.123.123";

        int year = VisitorLocalDateTime.YEAR;
        int month = VisitorLocalDateTime.MONTH;
        int day = VisitorLocalDateTime.DAY;

        visitorRedisUtil.increaseData("viewCount_Home_1"+year+month+day);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/visitor")
                        .header("X-FORWARDED-FOR", mockIp)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        int dataValue = jsonNode.get("data").asInt();

        //then
        assertThat(viewCountUtil.getViewCount("viewCount_Home_1"+year+month+day)).isEqualTo(dataValue);
    }

}