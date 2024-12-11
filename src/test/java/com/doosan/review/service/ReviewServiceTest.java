package com.doosan.review.service;

import com.doosan.review.entity.Product;
import com.doosan.review.entity.Review;
import com.doosan.review.repository.ProductRepository;
import com.doosan.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Rollback(false)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ReviewServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Long productId;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setReviewCount(0L);
        product.setScore(0.0f);
        product = productRepository.save(product);
        productId = product.getId();

        Review review1 = new Review();
        review1.setProduct(product);
        review1.setUserId(1L);
        review1.setScore(5);
        review1.setContent("이걸 사용하고 제 인생이 달라졌습니다.");
        review1.setImageUrl("/image.png");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setProduct(product);
        review2.setUserId(3L);
        review2.setScore(5);
        review2.setContent("이걸 사용하고 제 인생이 달라졌습니다.");
        review2.setImageUrl(null);
        reviewRepository.save(review2);
    }

    @Test
    @Rollback(false)
    void testReviewCount() {
        List<Review> reviews = reviewRepository.findByProductIdOrderByIdDesc(productId);
        assertThat(reviews.size()).isEqualTo(2);
    }

    @Test
    @Rollback(false)
    void testProductScore() {
        Product product = productRepository.findById(productId).orElseThrow();
        assertThat(product.getScore()).isEqualTo(0.0f);
    }
}
