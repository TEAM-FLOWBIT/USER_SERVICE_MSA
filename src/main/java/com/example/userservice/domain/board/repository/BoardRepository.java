package com.example.userservice.domain.board.repository;

import com.example.userservice.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> ,BoardQuerydslRepository{

}
