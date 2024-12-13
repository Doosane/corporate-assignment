package com.doosan.review.dto;

import com.doosan.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponse {
    private long totalCount;
    private double score;
    private long cursor;
    private List<ReviewDetail> reviews;

    public ReviewResponse(long totalCount, double score, long cursor, List<ReviewDetail> reviews) {
        this.totalCount = totalCount;
        this.score = score;
        this.cursor = cursor;
        this.reviews = reviews;
    }


}





