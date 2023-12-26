package com.example.userservice.domain.visitor.entity;

import com.example.userservice.util.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VisitorTest extends ControllerTestSupport {



    @DisplayName("조회 수는 초기값이 0이다.")
    @Test
    void test(){

        //when //given
        Visitor visitor =new Visitor();


        //then
        assertThat(visitor.getCount()).isEqualTo(0);
        assertThat(visitor.getId()).isZero();

    }
    @DisplayName("조회수 객체의 조회수는 더해진만큼 조회수가 증가한다.")
    @Test
    void plusViewCount() throws Exception {
        //given
        Visitor home = new Visitor(0);

        //when
        home.plus(10);

        //then
        assertThat(home.getCount()).isEqualTo(10);
    }


}