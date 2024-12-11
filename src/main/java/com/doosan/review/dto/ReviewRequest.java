package com.doosan.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequest {
    private Long userId;
    private int score;
    private String content;
}
