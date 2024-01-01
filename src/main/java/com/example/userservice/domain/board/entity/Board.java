package com.example.userservice.domain.board.entity;

import com.example.userservice.domain.member.entity.Member;
import com.example.userservice.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BOARD")
@Where(clause = "deleted_at IS NULL")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoardComment> boardComments=new ArrayList<>();

    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoardImage> boardImages= new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content,List<BoardComment> comments, Member member, List<BoardImage> boardImages) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardComments = comments;
        this.member = member;
        this.boardImages = boardImages;
    }
}
