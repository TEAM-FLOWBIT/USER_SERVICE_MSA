package com.example.userservice.domain.board.fixture;

import com.example.userservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.userservice.domain.member.dto.request.SignUpRequestDto;

public class BoardFixture {

    public static CreateBoardRequestDto createBoardRequestDto = CreateBoardRequestDto.builder()
            .title("제목입니다")
            .content("내용입니다")
            .build();
}
