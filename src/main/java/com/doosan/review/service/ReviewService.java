package com.doosan.review.service;

import com.doosan.review.dto.ReviewRequest;
import com.doosan.review.dto.ReviewResponse;
import com.doosan.review.entity.Product;
import com.doosan.review.entity.Review;
import com.doosan.review.repository.ProductRepository;
import com.doosan.review.repository.ReviewRepository;
import com.doosan.review.repository.S3Uploader;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

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


    @Transactional(readOnly = true)
    public ReviewResponse getReviews(Long productId, Long cursor, int size) {
        // 데이터베이스에서 커서 기반으로 페이징된 리뷰 목록 조회
        List<Review> reviews = (cursor == null)
                ? reviewRepository.findByProductIdOrderByIdDesc(productId, PageRequest.of(0, size))
                : reviewRepository.findByProductIdAndIdLessThanOrderByIdDesc(productId, cursor, PageRequest.of(0, size));

        // 총 리뷰 수 및 평균 점수 계산
        long totalCount = reviewRepository.countByProductId(productId);
        double averageScore = reviewRepository.calculateAverageScoreByProductId(productId);

        // 다음 요청에서 사용할 `cursor` 값 계산
        Long newCursor = reviews.size() < size ? -1 : reviews.get(reviews.size() - 1).getId();

        // 응답 생성
        return new ReviewResponse(totalCount, averageScore, newCursor, reviews);
    }

    @Transactional
    public void addReview(Long productId, ReviewRequest reviewRequest, MultipartFile image) {
        // 해당 상품이 존재하는지 확인
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        // 유저가 이미 해당 상품에 리뷰를 작성했는지 확인
        if (reviewRepository.existsByProductIdAndUserId(productId, reviewRequest.getUserId())) {
            throw new IllegalArgumentException("이미 해당 상품에 리뷰를 작성하셨습니다.");
        }

        // 이미지 업로드 dummy 구현체 사용
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Uploader.upload(image);
        }

        // 리뷰 엔티티 생성 및 저장
        Review review = new Review();
        review.setProduct(productOptional.get());
        review.setUserId(reviewRequest.getUserId());
        review.setScore(reviewRequest.getScore());
        review.setContent(reviewRequest.getContent());
        review.setImageUrl(imageUrl);
        reviewRepository.save(review);

        // 해당 상품의 리뷰 개수 및 평균 점수 업데이트
        Product product = productOptional.get();
        product.setReviewCount(product.getReviewCount() + 1);
        product.setScore((product.getScore() * (product.getReviewCount() - 1) + review.getScore()) / product.getReviewCount());
        productRepository.save(product);
    }
}
