package com.example.userservice.domain.board.dto.response;

import com.example.userservice.domain.board.entity.BoardComment;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreatedBoardCommentResponseDto {

    private Long boardId;
    private String content;
    private String memberId;
    private String name;
    private String profile;
    private Long commentId;

    @Builder
    public CreatedBoardCommentResponseDto(BoardComment boardComment) {
        this.boardId = boardComment.getBoard().getId();
        this.content = boardComment.getContent();
        this.memberId = boardComment.getMember().getUserId();
        this.name = boardComment.getMember().getName();
        this.profile = boardComment.getMember().getProfile();
        this.commentId=boardComment.getId();
    }
}
