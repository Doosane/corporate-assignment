package com.doosan.review.repository;

import com.doosan.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdOrderByIdDesc(Long productId); // 리뷰 Id 기준으로 내림차순 정렬
    boolean existsByProductIdAndUserId(Long productId, Long userId);
}