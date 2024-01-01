package com.example.userservice.domain.board.repository;

import com.example.userservice.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {

    List<BoardImage> deleteAllByBoard_Id(Long boardId);
}
