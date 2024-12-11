package com.doosan.review.controller;

import com.doosan.review.dto.ReviewRequest;
import com.doosan.review.dto.ReviewResponse;
import com.doosan.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ReviewResponse> getReviews(@PathVariable Long productId,
                                                     @RequestParam(value = "cursor", required = false) Long cursor,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        ReviewResponse reviewResponse = reviewService.getReviews(productId, cursor, size);
        return ResponseEntity.ok(reviewResponse);
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable Long productId,
                                          @RequestPart("review") ReviewRequest reviewRequest,
                                          @RequestPart(value = "image", required = false) MultipartFile image) {
        reviewService.addReview(productId, reviewRequest, image);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
