FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/review-V2.jar /app/review-V2.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/review-V2.jar"]
