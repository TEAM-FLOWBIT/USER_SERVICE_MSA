package com.example.userservice.domain.visitor.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long count;


    @Builder
    public Visitor(long count) {
        this.count = count;
    }

    public void plus(long viewCount) {
        this.count += viewCount;
    }
}