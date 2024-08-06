package com.likelion.timer.satisfatction;

import java.time.LocalDateTime;

import com.likelion.timer.timer.model.PartTypeEnum;
import com.likelion.timer.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "satisfaction")
public class Satisfaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 평점

    private String comment;

    @Enumerated(EnumType.STRING)
    private PartTypeEnum partType; // 부위

    private int year;
    private int month;
    private int day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Satisfaction(int rating, String comment, PartTypeEnum partType, int year, int month, int day, User user) {
        this.rating = rating;
        this.comment = comment;
        this.partType = partType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.year == 0) this.year = now.getYear();
        if (this.month == 0) this.month = now.getMonthValue();
        if (this.day == 0) this.day = now.getDayOfMonth();
    }

    // 추가 생성자
    public Satisfaction(Long id, int rating, String comment, PartTypeEnum partType, int year, int month, int day, User user) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.partType = partType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.user = user;
    }
}
