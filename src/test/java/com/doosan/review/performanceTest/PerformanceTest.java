//package com.doosan.review.performanceTest;
//
//import com.doosan.review.entity.Product;
//import com.doosan.review.entity.Review;
//import com.doosan.review.repository.ProductRepository;
//import com.doosan.review.repository.ReviewRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
//public class PerformanceTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    private Long productId;
//
//    @BeforeEach
//    void setUp() {
//        // ID가 1인 단일 상품 생성
//        Product product = new Product();
//        product.setId(1L); // 상품 ID를 1로 설정
//        product.setReviewCount(0L);
//        product.setScore(0.0f);
//        product = productRepository.save(product);
//        productId = product.getId();
//
//        // 1번 상품에 대한 대규모 리뷰 데이터 삽입
//        Product finalProduct = product;
//        IntStream.range(1, 10001).forEach(i -> {
//            Review review = new Review();
//            review.setProduct(finalProduct);
//            review.setUserId((long) i);
//            review.setScore(i % 5 + 1);
//            review.setContent("This is review number " + i);
//            review.setImageUrl(i % 2 == 0 ? "/image.png" : null);
//            reviewRepository.save(review);
//        });
//    }
//
//    @Test
//    void testLargeScalePerformance() {
//        long startTime = System.currentTimeMillis();
//
//        // 1번 상품에 대한 대규모 데이터 조회
//        List<Review> reviews = reviewRepository.findByProductIdOrderByIdDesc(productId, Pageable.unpaged());
//
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//
//        System.out.println("Time taken for querying large dataset: " + duration + " ms");
//        assertThat(reviews.size()).isEqualTo(10000);
//    }
//}
