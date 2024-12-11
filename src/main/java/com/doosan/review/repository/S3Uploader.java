package com.doosan.review.repository;

import org.springframework.web.multipart.MultipartFile;

public interface S3Uploader {
    String upload(MultipartFile file);
}

