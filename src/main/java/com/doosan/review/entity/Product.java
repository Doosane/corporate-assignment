package com.doosan.review.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewCount = 0L;

    @Column(nullable = false)
    private Float score = 0.0f;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // 순환 참조 방지
    private List<Review> reviews = new ArrayList<>();

    // 기본 생성자
    public Product() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // 리뷰 추가 메서드
    public void addReview(Review review) {
        reviews.add(review);
        review.setProduct(this);
    }

    // 리뷰 삭제 메서드
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setProduct(null);
    }
}
