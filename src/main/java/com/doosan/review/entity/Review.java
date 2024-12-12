package com.doosan.review.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "review")
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // 데이터베이스의 외래 키와 매핑
    @JsonBackReference // 순환 참조 방지
    private Product product;

    @Column(name = "user_id", nullable = false) // userId 매핑
    private Long userId;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String content;

    @Column(name = "image_url") // imageUrl 매핑
    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if(this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // 삽입 시 현재 시간 자동 설정
        }
    }
}
