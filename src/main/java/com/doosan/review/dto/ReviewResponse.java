package com.doosan.review.dto;

import com.doosan.review.entity.Review;
import java.util.List;

public class ReviewResponse {
    private long totalCount;
    private double score;
    private long cursor;
    private List<Review> reviews;

    public ReviewResponse(long totalCount, double score, long cursor, List<Review> reviews) {
        this.totalCount = totalCount;
        this.score = score;
        this.cursor = cursor;
        this.reviews = reviews;
    }

    // Getters and Setters
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getCursor() {
        return cursor;
    }

    public void setCursor(long cursor) {
        this.cursor = cursor;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
