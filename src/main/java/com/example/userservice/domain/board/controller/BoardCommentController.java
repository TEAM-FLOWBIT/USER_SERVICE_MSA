package com.example.userservice.domain.board.controller;


import com.example.userservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.userservice.domain.board.dto.response.CreatedBoardCommentResponseDto;
import com.example.userservice.domain.board.service.BoardCommentService;
import com.example.userservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/board/comment")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @PostMapping("")
    public ResponseEntity<CommonResDto<?>> createBoardComment(@RequestBody CreateBoardCommentRequestDto createBoardCommentRequestDto){

        CreatedBoardCommentResponseDto result = boardCommentService.createBoardComment(createBoardCommentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"게시글 댓글작성 성공",result));
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResDto<?>> deleteBoardComment(@PathVariable Long commentId){

        boardCommentService.deleteBoardComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"게시글 댓글삭제 성공",null));
    }


}
