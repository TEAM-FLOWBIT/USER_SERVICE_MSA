package com.example.userservice.domain.board.service;

import com.example.userservice.domain.board.dto.BoardSearchCondition;
import com.example.userservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.userservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.userservice.domain.board.dto.response.ReadBoardListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardResquestDto);

    Page<ReadBoardListResponseDto> readBoardList(Pageable pageable,BoardSearchCondition boardSearchCondition);

    void deleteBoard(Long boardId);
}
