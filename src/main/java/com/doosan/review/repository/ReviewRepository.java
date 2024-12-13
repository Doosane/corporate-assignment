package com.doosan.review.repository;

import com.doosan.review.dto.ReviewSummary; // DTO 클래스 ReviewSummary를 import
import com.doosan.review.entity.Review; // 엔티티 클래스 Review를 import

import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository를 상속받아 JPA 기능 제공
import org.springframework.data.jpa.repository.Query; // @Query 어노테이션 사용을 위한 import
import org.springframework.data.repository.query.Param; // @Param 어노테이션 사용을 위한 import

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //상품 ID를 기준으로 리뷰 통계와 리뷰 데이터를 반환하는 커스텀 쿼리 메서드.

    @Query("SELECT new com.doosan.review.dto.ReviewSummary (" +  // ReviewSummary 객체를 생성
            "r, COUNT(r), AVG(r.score)) " + // 리뷰 개수, 평균 평점
            "FROM Review r " +
            "WHERE r.product.id = :productId " +  // r.product.id`가 입력받은 `:productId`와 동일한 리뷰를 조회
            "GROUP BY r.product.id " +  // GROUP BY 절로 상품 ID별로 그룹화하여 통계를 계산
            "ORDER BY r.id DESC")  // ORDER BY 절로 리뷰 ID를 기준으로 내림차순 정렬
    List<ReviewSummary> findReviewSummaryByProductId(@Param("productId") Long productId);

    // 특정 상품에 특정 사용자가 이미 리뷰를 작성했는지 확인하는 메서드
    boolean existsByProductIdAndUserId(Long productId, Long userId);

}
