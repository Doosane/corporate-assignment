package com.doosan.review.repository;

import com.doosan.review.entity.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Pageable을 사용하는 메서드로 정의
    List<Review> findByProductIdOrderByIdDesc(Long productId, Pageable pageable);

    // 커서 기반 조회 메서드
    List<Review> findByProductIdAndIdLessThanOrderByIdDesc(Long productId, Long cursor, Pageable pageable);

    // 총 리뷰 개수 조회
    long countByProductId(Long productId);

    // 평균 점수 계산
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.product.id = :productId")
    double calculateAverageScoreByProductId(@Param("productId") Long productId);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

}