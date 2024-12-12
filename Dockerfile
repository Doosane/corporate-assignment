FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/review.jar /app/review.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/review.jar"]
