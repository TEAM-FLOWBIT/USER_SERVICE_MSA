package com.example.userservice.domain.board.controller;

import com.example.userservice.domain.board.dto.BoardSearchCondition;
import com.example.userservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.userservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.userservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.userservice.domain.board.service.BoardService;
import com.example.userservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/board")
public class BoardController {


    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<CommonResDto<?>> createBoard(@ModelAttribute CreateBoardRequestDto createBoardResquestDto){
        log.info("글 쓰기 APi 작동");
        CreateBoardResponseDto result = boardService.createBoard(createBoardResquestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1,"글 작성완료",result));
    }

    @GetMapping("")
    public ResponseEntity<CommonResDto<?>> boardList(BoardSearchCondition boardSearchCondition,
                                                     @PageableDefault(size=5, sort="board_id",direction = Sort.Direction.ASC) Pageable pageable){

        Page<ReadBoardListResponseDto> result = boardService.readBoardList(pageable,boardSearchCondition);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"글 리스트 조회 성공",result));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResDto<?>>  deleteBoard(@PathVariable Long boardId){

        boardService.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"커뮤니티 삭제 성공",null));
    }
}
