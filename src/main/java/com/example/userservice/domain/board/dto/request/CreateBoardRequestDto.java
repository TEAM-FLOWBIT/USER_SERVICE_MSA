package com.example.userservice.domain.board.dto.request;

import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.member.entity.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CreateBoardRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @Column(nullable = false)
    List<MultipartFile> pictures;

    public Board toEntity(CreateBoardRequestDto createBoardRequestDto, Member member) {
        return Board.builder()
                .content(createBoardRequestDto.getContent())
                .title(createBoardRequestDto.getTitle())
                .member(member)
                .build();
    }
}

