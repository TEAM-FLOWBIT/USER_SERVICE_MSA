package com.example.userservice.domain.board.entity;


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
@Table(name = "BOARD_IMAGE")
@Where(clause = "deleted_at IS NULL")
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_IMAGE_ID")
    private Long id;

    @Column(nullable = false)
    private String image;

    @JoinColumn(name="BOARD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public BoardImage(Long id, String image, Board board) {
        this.id = id;
        this.image = image;
        this.board = board;
    }
}
