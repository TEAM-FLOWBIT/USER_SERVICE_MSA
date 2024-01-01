package com.example.userservice.domain.board.entity;


import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BOARD_COMMENT")
@Where(clause = "deleted_at IS NULL")
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name="BOARD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public BoardComment(Long id, String content, Board board, Member member) {
        this.id = id;
        this.content = content;
        this.board = board;
        this.member = member;
    }
}
