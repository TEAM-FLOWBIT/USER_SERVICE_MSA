package com.example.userservice.domain.board.controller;

import com.example.userservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.board.entity.BoardComment;
import com.example.userservice.domain.board.repository.BoardCommentRepository;
import com.example.userservice.domain.board.repository.BoardRepository;
import com.example.userservice.domain.board.service.BoardService;
import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.domain.member.service.MemberService;
import com.example.userservice.util.ControllerTestSupport;
import com.example.userservice.util.ImageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.apache.commons.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BoardControllerTest extends ControllerTestSupport {



    @Autowired
    BoardService boardService;
    @Autowired
    BoardCommentRepository boardCommentRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberService memberService;
    private Member testUser;
    private Member testUser2;

    @BeforeEach
    void setUp() {
        testUser = Member.builder()
                .phone("010-1234-1234")
                .name("김민우1234")
                .password("anstn1234@")
                .userId("testUser")
                .nickname("민우닉네임")
                .profile("flowbit-default-profile.png")
                .state(true)
                .build();
        testUser2=Member.builder()
                .phone("010-1234-1234")
                .name("김민우1234")
                .password("anstn1234@")
                .userId("testUser2")
                .nickname("민우닉네임2")
                .profile("flowbit-default-profile.png")
                .state(true)
                .build();

        memberRepository.saveAndFlush(testUser);
    }


    @DisplayName("로그인을 한 유저는 커뮤니티에 게시글을 이미지 없이 작성할 수 있다.")
    @WithMockUser(username = "testUser")
    @Test
    void creaetBoard() throws Exception {

        //given


        String url = "/api/v1/board";
        //when //then
        mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("title", "제목입니다")
                        .param("content", "내용입니다"))
                .andDo(print())
                .andExpect(status().isCreated());

    }
    @DisplayName("로그인을 한 유저는 커뮤니티에 게시글을 이미지와 함께 작성할 수 있다.")
    @WithMockUser(username = "testUser")
    @Test
    void creaetBoardWithImageFile() throws Exception {

        //given

        List<MockMultipartFile> multiFiles = List.of(
                ImageUtil.generateMockImageFile("subFiles"),
                ImageUtil.generateMockImageFile("subFiles")
        );


        String url = "/api/v1/board";
        //when //then
        MvcResult mvcResult = mockMvc.perform(
                        multipart(url)
                                .file(multiFiles.get(0))
                                .file(multiFiles.get(1))
                                .param("title","제목입니다")
                                .param("content","내용입니다")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }

    @DisplayName("로그인을 한 유저는 커뮤니티 게시글에 댓글을 작성 할 수 있다.")
    @WithMockUser(username = "testUser")
    @Test
    void createBoardComment() throws Exception {

        //given
        String url = "/api/v1/board/comment";

        Board board = Board.builder()
                .title("제목")
                .member(testUser)
                .content("내용")
                .build();
       boardRepository.saveAndFlush(board);

        CreateBoardCommentRequestDto createBoardCommentRequestDto= CreateBoardCommentRequestDto.builder()
                .boardId(board.getId())
                .content("댓글입니다")
                .build();


        
        //when //then
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(createBoardCommentRequestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


    }
    @DisplayName("로그인을 한 유저는 커뮤니티 게시글에 자신이 등록한 댓글을 삭제 할 수 있다.")
    @WithMockUser(username = "testUser")
    @Test
    void deleteBoardComment() throws Exception {

        //given
        Board board= Board.builder()
                .title("제목")
                .member(testUser)
                .content("내용")
                .build();
        BoardComment boardComment = BoardComment.builder()
                .board(board)
                .member(testUser)
                .content("댓글")
                .build();
        boardRepository.saveAndFlush(board);
        boardCommentRepository.saveAndFlush(boardComment);


        String url = "/api/v1/board/comment/"+boardComment.getId();
        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


    }
    @DisplayName("로그인을 한 유저는 자신이 등록한 커뮤니티를 삭제 할 수 있다.")
    @WithMockUser(username = "testUser")
    @Test
    void deleteBoard() throws Exception {

        //given
        Board board= Board.builder()
                .title("제목")
                .member(testUser)
                .content("내용")
                .build();
        boardRepository.saveAndFlush(board);
        String url = "/api/v1/board/"+board.getId();
        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @DisplayName("로그인을 하지않은 사용자는 게시글을 작성할 수 없다.")
    @Test
    void unauthenticatedUserCannotCreateBoard() throws Exception {

        //given
        String url = "/api/v1/board";
        //when //then
        mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("title", "제목입니다")
                        .param("content", "내용입니다"))
                .andDo(print())
                .andExpect(status().isUnauthorized());


    }

    @DisplayName("사용자는 다른유저의 게시글을 삭제할 수 없다.")
    @Test
    @WithMockUser(username="testUser2")
    void loggedInUserCannotDeleteOtherUserBoard() throws Exception {
        //given
        Board board= Board.builder()
                .title("제목")
                .member(testUser)
                .content("내용")
                .build();
        boardRepository.saveAndFlush(board);
        String url = "/api/v1/board/"+board.getId();
        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();


    }
    @DisplayName("사용자는 다른유저의 댓글을 삭제할 수 없다.")
    @Test
    @WithMockUser(username="testUser2")
    void loggedInUserCannotDeleteOtherUserBoardComment() throws Exception {

        //given
        Board board= Board.builder()
                .title("제목")
                .member(testUser)
                .content("내용")
                .build();
        BoardComment boardComment = BoardComment.builder()
                .board(board)
                .member(testUser)
                .content("댓글")
                .build();
        boardRepository.saveAndFlush(board);
        boardCommentRepository.saveAndFlush(boardComment);


        String url = "/api/v1/board/comment/"+boardComment.getId();
        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();



    }

}