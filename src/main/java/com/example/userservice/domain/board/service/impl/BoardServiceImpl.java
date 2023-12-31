package com.example.userservice.domain.board.service.impl;


import com.example.userservice.domain.board.dto.BoardSearchCondition;
import com.example.userservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.userservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.userservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.board.entity.BoardImage;
import com.example.userservice.domain.board.repository.BoardCommentRepository;
import com.example.userservice.domain.board.repository.BoardImageRepository;
import com.example.userservice.domain.board.repository.BoardRepository;
import com.example.userservice.domain.board.service.BoardService;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.global.aws.AwsS3Service;
import com.example.userservice.global.exception.error.BoardNotFoundException;
import com.example.userservice.global.exception.error.UnAuthorizedException;
import com.example.userservice.global.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final UserHelper userHelper;
    private final AwsS3Service awsS3Service;


    @Override
    @Transactional
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto) {
        Member member = userHelper.getMember();
        List<String> uploadedPaths = new ArrayList<>();

        Board board = createBoardRequestDto.toEntity(createBoardRequestDto, member);
        Board savedBoard = boardRepository.save(board);

        if (createBoardRequestDto.getPictures() != null && !createBoardRequestDto.getPictures().isEmpty()) {
            //aws upload
            uploadedPaths = uploadImages(createBoardRequestDto, member);
            // image 저장
            List<BoardImage> boardImageList = boardImageToEntity(uploadedPaths, savedBoard);
            boardImageRepository.saveAll(boardImageList);
        }

        return CreateBoardResponseDto.builder()
                .board(savedBoard)
                .member(member)
                .imagePaths(uploadedPaths)
                .build();
    }

    @Override
    @Transactional
    public Page<ReadBoardListResponseDto> readBoardList(Pageable pageable,BoardSearchCondition boardSearchCondition) {
        Page<Board> boards = boardRepository.readBoardList(pageable,boardSearchCondition);
        List<ReadBoardListResponseDto> dtoList = boards.stream()
                .map(board -> ReadBoardListResponseDto.builder().board(board).build())
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, boards.getSize());
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException());
        Member member = userHelper.getMember();
        if(board.getMember().getId().equals(member.getId())){
            // Delete images
            List<BoardImage> boardImages = boardImageRepository.deleteAllByBoard_Id(boardId);
            boardImages.forEach(boardImage -> awsS3Service.deleteFile(boardImage.getImage()));

            // Delete comments
            boardCommentRepository.deleteAllByBoard_Id(boardId);

            // Delete board
            boardRepository.deleteById(boardId);
        }else{
            throw new UnAuthorizedException("삭제할 권한이 없습니다");
        }


    }

    private static List<BoardImage> boardImageToEntity(List<String> uploadedPaths, Board savedBoard) {

        List<BoardImage> boardImageList = uploadedPaths.stream().map(image -> BoardImage.builder()
                .board(savedBoard)
                .image(image)
                .build()).collect(Collectors.toList());
        return boardImageList;
    }

    private List<String> uploadImages(CreateBoardRequestDto createBoardResquestDto, Member member) {
        List<MultipartFile> pictures = createBoardResquestDto.getPictures();
        for (MultipartFile picture : pictures) {
            log.info(picture.getOriginalFilename());
        }

        List<String> uploadedPaths = createBoardResquestDto.getPictures().stream().map(image -> {
            try {
                return awsS3Service.upload(image.getOriginalFilename(), image, member.getUserId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return uploadedPaths;
    }
}
