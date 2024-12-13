package com.doosan.review.service;

import com.doosan.review.dto.ReviewDetail;
import com.doosan.review.dto.ReviewRequest;
import com.doosan.review.dto.ReviewResponse;
import com.doosan.review.dto.ReviewSummary;
import com.doosan.review.entity.Product;
import com.doosan.review.entity.Review;
import com.doosan.review.repository.ProductRepository;
import com.doosan.review.repository.ReviewRepository;
import com.doosan.review.repository.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final S3Uploader s3Uploader;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, S3Uploader s3Uploader) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.s3Uploader = s3Uploader;
    }

    // 리뷰 조회 서비스 로직
    @Transactional(readOnly = true)
    public ReviewResponse getReviewsByProductId(Long productId, int limit, Long cursor) {
        // 상품별 리뷰를 그룹화하여 조회
        List<ReviewSummary> summaries = reviewRepository.findReviewSummaryByProductId(productId);

        // 해당 상품에 리뷰가 없는 경우 기본값 반환
        if (summaries.isEmpty()) {
            return new ReviewResponse(0, 0.0, 0, List.of());
        }

        // 첫 번째 ReviewSummary 추출
        ReviewSummary summary = summaries.get(0);

        // 총 리뷰 개수와 평균 평점을 계산
        long totalCount = summary.getReviewCount();
        double score = summary.getAverageScore();

        // 리뷰 데이터를 ReviewDetail DTO로 변환
        List<ReviewDetail> reviewDetails = summary.getReviews().stream()
                .map(review -> new ReviewDetail(
                        review.getId(),
                        review.getUserId(),
                        review.getScore(),
                        review.getContent(),
                        review.getImageUrl(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());

        // 마지막 리뷰 ID를 다음 커서 값으로 설정
        long lastCursor = reviewDetails.isEmpty() ? 0 : reviewDetails.get(reviewDetails.size() - 1).getId();

        // 결과 반환
        return new ReviewResponse(totalCount, score, lastCursor, reviewDetails);
    }

    // 등록
    @Transactional
    public void addReview(Long productId, ReviewRequest reviewRequest, MultipartFile image) {
        // 상품 존재 여부 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 해당 상품에 동일 사용자가 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByProductIdAndUserId(productId, reviewRequest.getUserId())) {
            throw new IllegalArgumentException("이미 해당 상품에 리뷰를 작성하셨습니다.");
        }

        // 이미지 업로드 처리
        String imageUrl = uploadImage(image);

        // 새로운 리뷰 객체 생성 및 저장
        Review review = new Review();
        review.setProduct(product);
        review.setUserId(reviewRequest.getUserId());
        review.setScore(reviewRequest.getScore());
        review.setContent(reviewRequest.getContent());
        review.setImageUrl(imageUrl);
        reviewRepository.save(review);

        // 상품 리뷰 통계 업데이트
        updateProductStats(product, review.getScore());
    }

    // 이미지 파일을 업로드하고 업로드된 URL 반환
    private String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return s3Uploader.upload(image);
    }

    // 상품 리뷰 통계 업데이트
    private void updateProductStats(Product product, int newScore) {
        int oldReviewCount = Math.toIntExact(product.getReviewCount()); // 기존 리뷰 개수와 새로운 리뷰 개수 계산
        int newReviewCount = oldReviewCount + 1;

        double oldAverageScore = product.getScore(); // 기존 평균 평점과 새로운 평균 평점 계산
        double newAverageScore = ((oldAverageScore * oldReviewCount) + newScore) / newReviewCount;

        product.setReviewCount((long) newReviewCount);  // 상품 정보 업데이트
        product.setScore((float) newAverageScore);
        productRepository.save(product);
    }
}
