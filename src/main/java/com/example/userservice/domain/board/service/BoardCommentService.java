package com.example.userservice.domain.board.service;

import com.example.userservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.userservice.domain.board.dto.response.CreatedBoardCommentResponseDto;

public interface BoardCommentService {

    CreatedBoardCommentResponseDto createBoardComment(CreateBoardCommentRequestDto createBoardCommentRequestDto);

    void deleteBoardComment(Long commentId);
}
