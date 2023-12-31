package com.example.userservice.domain.board.service.impl;

import com.example.userservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.userservice.domain.board.dto.response.CreatedBoardCommentResponseDto;
import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.board.entity.BoardComment;
import com.example.userservice.domain.board.repository.BoardCommentRepository;
import com.example.userservice.domain.board.repository.BoardRepository;
import com.example.userservice.domain.board.service.BoardCommentService;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.global.exception.error.BoardCommentNotFoundException;
import com.example.userservice.global.exception.error.BoardNotFoundException;
import com.example.userservice.global.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {


    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final UserHelper userHelper;
    @Override
    @Transactional
    public CreatedBoardCommentResponseDto createBoardComment(CreateBoardCommentRequestDto createBoardCommentRequestDto) {
        Long boardId = createBoardCommentRequestDto.getBoardId();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("게시글이 존재하지 않습니다"));
        Member member = userHelper.getMember();


        BoardComment boardComment = createBoardCommentRequestDto.toEntity(member, board, createBoardCommentRequestDto.getContent());
        BoardComment savedBoardComment = boardCommentRepository.save(boardComment);

        return CreatedBoardCommentResponseDto.builder()
                .boardComment(savedBoardComment)
                .build();
    }

    @Override
    @Transactional
    public void deleteBoardComment(Long commentId) {
        Member member = userHelper.getMember();
        BoardComment boardComment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardCommentNotFoundException("댓글을 찾지 못했습니다"));

        if(boardComment.getMember().getId().equals(member.getId())) {
            boardCommentRepository.deleteById(commentId);
        }

    }
}
