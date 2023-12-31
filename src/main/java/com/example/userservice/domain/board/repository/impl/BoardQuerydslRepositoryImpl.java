package com.example.userservice.domain.board.repository.impl;

import com.example.userservice.domain.board.dto.BoardSearchCondition;
import com.example.userservice.domain.board.entity.Board;
import com.example.userservice.domain.board.repository.BoardQuerydslRepository;
import com.example.userservice.global.config.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.userservice.domain.board.entity.QBoard.board;


public class BoardQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BoardQuerydslRepository {


    public BoardQuerydslRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> readBoardList(Pageable pageable,BoardSearchCondition boardSearchCondition) {
        JPAQuery<Board> contentQuery = new JPAQueryFactory(getEntityManager()).
        selectFrom(board)
                .where(searchWordExpression(boardSearchCondition.getSearchword()))
                .offset(pageable.getOffset())
                .orderBy(board.createdAt.desc())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = new JPAQueryFactory(getEntityManager())
                .select(board.count())
                .from(board)
                .where(searchWordExpression(boardSearchCondition.getSearchword()));

        return PageableExecutionUtils.getPage(contentQuery.fetch(),pageable, countQuery::fetchCount);
    }


    /**
     * 검색어 단어를 통한 동적검색
     */
    private BooleanExpression searchWordExpression(String searchWord) {

        return Optional.ofNullable(searchWord) //seachWord가 null이 아닌경우에 Optional로 감싸기
                .filter(word->!word.isEmpty()) // searchWord가 비어 있지 않은경우에만 map 함수
                .map(word-> Stream.of(board.content.containsIgnoreCase(word),
                                board.title.containsIgnoreCase(word))
                        .reduce(BooleanExpression::or) // 위 조건들을 OR 연산으로 묶음
                        .orElse(null))
                .orElse(null);//  // 만약 조건이 없으면 null 반환

    }
}
