package com.example.userservice.domain.visitor.util;

import com.example.userservice.domain.visitor.fixture.VisitorLocalDateTime;
import com.example.userservice.global.config.redis.util.VisitorRedisUtil;
import com.example.userservice.util.ControllerTestSupport;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ViewCountUtilTest extends ControllerTestSupport {

    @Autowired
    private ViewCountUtil viewCountUtil;


    @AfterEach
    void tearDown() {
        viewCountUtil.deleteData("ViewCount_Home_Total");
    }

    @DisplayName("redis에 데이터가 없을 경우에는 0을 반환한다.")
    @Test
    void readToDayViewCount(){
        //when
        Long viewCountHomeTotal = viewCountUtil.getViewCount("ViewCount_Home_Total");


        //then
        assertThat(viewCountHomeTotal).isEqualTo(0);



    }

    @DisplayName("redis에서 총 조회수를 증가시킨다.")
    @Test
    void increaseTotalViewCount(){

        //given
        viewCountUtil.increaseData("ViewCount_Home_Total");

        //when
        Long viewCountHomeTotal = viewCountUtil.getViewCount("ViewCount_Home_Total");
        // then
        
        assertThat(viewCountHomeTotal).isEqualTo(1);


    }

}