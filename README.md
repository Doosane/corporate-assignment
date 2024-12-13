이슈 관리
https://github.com/Doosane/corporate-assignment/issues

풀리퀘스트
https://github.com/Doosane/corporate-assignment/pulls

10만개 대용량 데이트 트래픽 
![image](https://github.com/user-attachments/assets/4e7b69df-d7be-457b-bbae-b80231d6fb81)


2024-12-13
### N + 1 오류 수정
하나의 쿼리로 리뷰 목록을 가져온 뒤, 각 리뷰마다 개별적으로 연관 데이터를 조회하는 추가 쿼리가 실행되던 오류 수정

Product와 Review 데이터를 각각 필요할 때만 조회하며, 연관 데이터를 추가적으로 가져오기 위한 쿼리가 실행되지 않도록 변경

### 수정 사항
ReviewController.java :

1. @GetMapping("/{productId}/reviews") → @GetMapping("/{productId}")
리뷰 조회 경로를 간소화하여 /reviews를 제거.

2.size → limit (페이징 크기 매개변수 이름 변경).

ReviewDetail.java :
1. createdAt 처리:
   Review 엔티티를 사용한 생성자에서 LocalDateTime 기반 생성자로 변경

ReviewRepository.java :
1. Pageable 기반 메서드 추가

2024-12-12
### 글로벌 예외 처리 추가

### 수정 사항
1. ErrorDetails.java:
   - status: 상태 코드 노출
   - error: 에러 유형 노출
   - message: 에러 메시지 내용 노출
   - path: 요청 경로 노출

2. GlobalExceptionHandler.java:
   - ResourceNotFoundException: "리소스를 찾을 수 없습니다."
   - IllegalArgumentException: "잘못된 요청입니다."
   - DataIntegrityViolationException: "데이터 충돌이 발생했습니다."
   - Exception: "서버 오류가 발생했습니다."
   - IllegalStateException: "상태 충돌이 발생했습니다."
   - MethodArgumentNotValidException: "유효성 검사 실패"
   - 각 예외에 대해 커스텀 메시지 추가

3. ResourceNotFoundException.java:
   - ResourceNotFoundException: 에러 메시지 노출되도록 수정

### 추가 수정 사항
- 코드 변경 포함
- build.gradle 버전명 변경 내용
- docker-compose.yml version: '3.8' 삭제 내용
- Dockerfile : 빌드한 자르파일명 변경 내용
- DateTimeFormat.java : ISO_DATE_TIME
   yyyy-MM-dd'T'HH:mm:ss.SSS'Z' 형태로 노출 위해

### [대용량 처리 개선] hicariCP db connectionpoll 추가

### 내용
[대용량 처리 개선] hicariCP db connectionpoll 추가

### 수정 사항
application.properties 에 HikariCP 설정 추가
대용량 처리 테스트를 위한 testcode 작성

### Review 리펙토링

### 내용
@JsonFormat 및 @Column 애노테이션 수정
ReviewResponse 와 ReviewDetail로 DTO분리

### 수정 사항
@JsonFormat 애노테이션에서 timezone 속성을 제거하고 로컬 타임존 형식으로 설정.
@Column 애노테이션에서 columnDefinition 속성을 제거하여 기본값 및 세부 설정을 간소화.
역할을 명확히 하기위해, 데이터 전송 최적화 위해 dto 분리

### 결과
createdAt 필드에 대한 날짜/시간 형식이 간소화
불필요한 기본값 설정 제거
재사용성과 확장성 향상

### Docker 환경 설정 추가

### 내용
docker-compose 기반으로 필요한 인프라 설정

### 수정 사항
docker-compose.yml 추가
Dockerfile 추가

### 결과
Docker를 사용하여 애플리케이션을 컨테이너화
로컬 환경에서 일관된 개발환경 구축

### JSON 날짜 포맷을 변경

### 문제 설명
과제의 리뷰 조회 API 응답 예시에서 createdAt 필드가 "createdAt": "2024-11-25T00:00:00.000Z" 이렇게 되어있고 이 형식은 ISO-8601 표준을 따르는 대표적인 날짜/시간 표현이므로 변경 필요

### 수정 사항
JacksonConfig.java를 추가하여 ObjectMapper를 전역적으로 설정:
JavaTimeModule 등록 및 ISO-8601 포맷 적용.
타임스탬프 대신 문자열로 날짜 직렬화.

Review.java 엔터티의 createdAt 필드에 @JsonFormat 추가:
ISO-8601 형식(yyyy-MM-dd'T'HH:mm:ss.SSS'Z')으로 JSON 직렬화.
UTC 타임존을 명시하여 날짜 형식 표준화.

### 결과
API 응답에서 createdAt 필드가 ISO-8601 표준을 준수
Jackson의 글로벌 설정을 통해 JSON 직렬화 로직을 중앙화하여 유지보수성 향상

### ReviewService 페이징 처리 및 커서 값 계산 오류 수정

### 문제 설명
ReviewService에서 커서 기반 페이징 처리가 제대로 작동하지 않아, cursor 값이 올바르게 설정되지 않음.
ReviewRepository의 findByProductIdOrderByIdDesc 메서드를 사용할 때, Pageable 인수를 전달하지 않아 오류 발생.

### 원인
페이징 처리를 메모리 내에서 수행하면서 발생한 성능 저하 및 정확하지 않은 커서 값 계산.
findByProductIdOrderByIdDesc 메서드가 Pageable 인수를 필요로 함에도 불구하고, 해당 인수가 전달되지 않아 발생한 오류.

### 수정 사항
ReviewService 클래스에서 findByProductIdOrderByIdDesc 및 findByProductIdAndIdLessThanOrderByIdDesc 메서드 호출 시 PageRequest를 사용하여 페이징 처리.
ReviewRepository 인터페이스에 페이징을 위한 메서드 추가.
커서 값을 올바르게 계산하여 다음 요청에서 사용할 수 있도록 수정.
ReviewServiceTest 클래스에서 페이징 및 점수 계산에 대한 테스트 케이스 추가.

### 결과
ReviewService에서 커서 기반 페이징이 올바르게 작동하여, 정확한 cursor 값이 반환됨.
ReviewRepository에서 페이징 기능이 정상적으로 작동하여, 성능이 개선되고 오류가 해결됨.
테스트 케이스를 통해 페이징 및 점수 계산이 정상적으로 이루어짐을 검증.


2024-12-11


### 문제 설명
리뷰 조회 시 정렬 기준이 CreatedAt(생성일)로 잘못 설정되어 있어,
요구사항과 일치하지 않음.

### 해결 내용
- 정렬 기준을 ID 내림차순으로 변경.
- Repository 메서드 수정: `findByProductIdOrderByCreatedAtDesc` → `findByProductIdOrderByIdDesc`.

### 추가 확인 사항
- Service 계층에서 수정된 Repository 메서드 호출 정상 확인.

S3 업로드 유틸 클래스 추가
review service , service test 추가
컨트롤러 추가 , DTO 추가
메인 어플리케이션 셋업 , 테스트 커밋
엔터티 추가 , 레포지토리 추가
초기프로젝트 셋업 , Gradle configuration







  
