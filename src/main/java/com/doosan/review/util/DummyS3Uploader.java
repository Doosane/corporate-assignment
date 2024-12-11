package com.doosan.review.util;

import com.doosan.review.repository.S3Uploader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DummyS3Uploader implements S3Uploader {
    @Override
    public String upload(MultipartFile file) {
        // 실제 S3 업로드 대신 더미 URL 반환
        return "https://dummy-s3-url.com/" + file.getOriginalFilename();
    }
}

