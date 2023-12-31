package com.example.userservice.domain.board.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardSearchCondition {

    private String searchword;
}
