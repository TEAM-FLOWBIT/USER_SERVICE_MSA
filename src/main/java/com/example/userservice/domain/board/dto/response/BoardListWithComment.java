package com.example.userservice.domain.board.dto.response;


import com.example.userservice.domain.board.entity.BoardComment;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BoardListWithComment {
    private String memberEmail;
    private String profile;
    private String createTime;
    private String content;
    private Long commentId;
    private Long memberId;
    private String name;

    @Builder
    public BoardListWithComment(BoardComment boardComment) {
        this.memberEmail=boardComment.getMember().getUserId();
        this.memberId = boardComment.getMember().getId();
        this.profile = boardComment.getMember().getProfile();
        this.createTime = String.valueOf(boardComment.getCreatedAt());
        this.content = boardComment.getContent();
        this.commentId=boardComment.getId();
        this.name=boardComment.getMember().getName();
    }
}