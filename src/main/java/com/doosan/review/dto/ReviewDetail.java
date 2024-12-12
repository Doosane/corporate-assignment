package com.doosan.review.dto;

import com.doosan.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDetail {
    private Long id;
    private Long userId;
    private int score;
    private String content;
    private String imageUrl;
    private String createdAt;

    public ReviewDetail(Review review) {
        this.id = review.getId();
        this.userId = review.getUserId();
        this.score = review.getScore();
        this.content = review.getContent();
        this.imageUrl = review.getImageUrl();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.createdAt = review.getCreatedAt().format(formatter);
    }
}

