package com.example.userservice.domain.visitor.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime visitDate;

    private long count;

    private String visitorIp;

    @Builder
    public Visitor(LocalDateTime visitDate, long count, String visitorIp) {
        this.visitDate = visitDate;
        this.count = count;
        this.visitorIp = visitorIp;
    }

    public void plus(long viewCount) {
        this.count += viewCount;
    }
}