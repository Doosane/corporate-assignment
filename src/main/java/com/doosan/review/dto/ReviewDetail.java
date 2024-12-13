package com.doosan.review.dto;

import com.doosan.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    public ReviewDetail(Long id, Long userId, int score, String content, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}



