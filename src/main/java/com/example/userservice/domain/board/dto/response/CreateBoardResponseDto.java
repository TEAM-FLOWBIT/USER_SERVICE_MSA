package com.example.userservice.domain.board.dto.response;

import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.member.entity.Member;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreateBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String memberId;
    private String memberNickname;
    private List<String> imagePaths;


    @Builder
    public CreateBoardResponseDto(Board board, Member member,List<String> imagePaths) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberId=member.getUserId();
        this.memberNickname=member.getNickname();
        this.imagePaths=imagePaths;
    }
}
