package com.example.userservice.domain.board.repository;

import com.example.userservice.domain.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {

    List<BoardComment> deleteAllByBoard_Id(Long boardId);

    List<BoardComment> findAllByBoard_Id(Long boardId);
}
