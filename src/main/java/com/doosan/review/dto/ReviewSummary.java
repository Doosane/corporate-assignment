package com.doosan.review.dto;

import com.doosan.review.entity.Review;
import io.micrometer.common.KeyValues;
import lombok.Getter;
import java.util.List;

@Getter
public class ReviewSummary {
    private List<Review> reviews;
    private long reviewCount;
    private double averageScore;

    public ReviewSummary(Review review, long reviewCount, double averageScore) {
        this.reviews = List.of(review); // 단일 리뷰를 리스트로 래핑
        this.reviewCount = reviewCount;
        this.averageScore = averageScore;
    }


}
