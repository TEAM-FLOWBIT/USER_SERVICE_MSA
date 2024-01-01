package com.example.userservice.domain.board.dto.request;

import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.board.entity.BoardComment;
import com.example.userservice.domain.member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBoardCommentRequestDto {
    @NotBlank
    private Long boardId;
    @NotBlank
    private String content;


    public BoardComment toEntity(Member member, Board board, String content) {
        return BoardComment.builder()
                .board(board)
                .member(member)
                .content(content)
                .build();
    }
}
