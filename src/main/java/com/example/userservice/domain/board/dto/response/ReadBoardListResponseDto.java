package com.example.userservice.domain.board.dto.response;


import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.board.entity.BoardImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ReadBoardListResponseDto {
    private Long memberId;
    private String memberEmail;
    private String name;
    private String profile;
    private Long boardId;
    private String title;
    private String content;
    private String createTime;
    private List<String> imagePath;
    private List<BoardListWithComment> comments=new ArrayList<>();

    @Builder
    public ReadBoardListResponseDto(Board board) {
        this.memberEmail=board.getMember().getUserId();
        this.memberId =board.getMember().getId();
        this.name = board.getMember().getName();
        this.profile = board.getMember().getProfile();
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createTime = String.valueOf(board.getCreatedAt());
        this.imagePath = board.getBoardImages().stream().map(BoardImage::getImage).collect(Collectors.toList());
        this.comments = board.getBoardComments().stream().map(BoardListWithComment::new).collect(Collectors.toList());
    }
}
