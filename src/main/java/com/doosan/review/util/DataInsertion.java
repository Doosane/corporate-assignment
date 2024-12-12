package com.doosan.review.util;

/*
            "1, 15, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image1.png', '2024-11-25T00:00:00.000Z'",
                "1, 2, 4, '이걸 사용하고 제 인생이 달라졌습니다.', '/image2.png', '2024-11-25T00:00:00.000Z'",
                "1, 14, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image3.png', '2024-11-25T00:00:00.000Z'",
                "1, 4, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image3.png', '2024-11-25T00:00:00.000Z'",
                "1, 5, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image3.png', '2024-11-25T00:00:00.000Z'",
                "1, 6, 4, '이걸 사용하고 제 인생이 달라졌습니다.', '/image3.png', '2024-11-25T00:00:00.000Z'",
                "1, 7, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image3.png', '2024-11-25T00:00:00.000Z'",
                "1, 8, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image14.png', '2024-11-25T00:00:00.000Z'",
                "1, 9, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image14.png', '2024-11-25T00:00:00.000Z'",
                "1, 10, 4, '이걸 사용하고 제 인생이 달라졌습니다.', '/image14.png', '2024-11-25T00:00:00.000Z'",
                "1, 11, 4, '이걸 사용하고 제 인생이 달라졌습니다.', '/image14.png', '2024-11-25T00:00:00.000Z'",
                "1, 12, 4, '이걸 사용하고 제 인생이 달라졌습니다.', '/image14.png', '2024-11-25T00:00:00.000Z'",
                "1, 13, 4, '이걸 사용하고 제 인생이 달라졌습니다.', '/image14.png', '2024-11-25T00:00:00.000Z'",
                "1, 3, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 1, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image.png', '2024-11-25T00:00:00.000Z'"
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataInsertion {
    public static void main(String[] args) {
        // 더미 데이터 리스트
        List<String> data = List.of(
                "1, 15, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 2, 4, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 14, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 4, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 5, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 6, 4, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 7, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 8, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 9, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 10, 4, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 11, 4, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 12, 4, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 13, 4, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 3, 5, '이걸 사용하고 제 인생이 달라졌습니다.', NULL, '2024-11-25T00:00:00.000Z'",
                "1, 1, 5, '이걸 사용하고 제 인생이 달라졌습니다.', '/image.png', '2024-11-25T00:00:00.000Z'"
        );

        // 문자열을 LocalDateTime으로 변환하고 MySQL이 이해할 수 있는 형식으로 변환
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> formattedData = new ArrayList<>();
        for (String item : data) {
            String[] parts = item.split(", ");
            String createdAtStr = parts[5].replace("'", "");
            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, inputFormatter);
            String formattedCreatedAt = createdAt.format(outputFormatter);
            parts[5] = "'" + formattedCreatedAt + "'";
            formattedData.add("(" + String.join(", ", parts) + ")");
        }

        // 최종 SQL 쿼리 생성
        String sql = "INSERT INTO review (product_id, user_id, score, content, image_url, created_at)\nVALUES\n    " +
                String.join(",\n    ", formattedData) + ";";

        System.out.println(sql); // 쿼리 확인용 출력

        // JDBC를 사용하여 데이터베이스에 연결하고 쿼리 실행
        String url = "jdbc:mysql://localhost:3306/review";
        String username = "root";
        String password = "admin12345";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("데이터 들어감");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



