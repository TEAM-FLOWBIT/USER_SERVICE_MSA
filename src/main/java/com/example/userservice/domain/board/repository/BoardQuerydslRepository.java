package com.example.userservice.domain.board.repository;


import com.example.userservice.domain.board.dto.BoardSearchCondition;
import com.example.userservice.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQuerydslRepository {

    Page<Board> readBoardList(Pageable pageable,BoardSearchCondition boardSearchCondition);
}
