FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/snapshot01.jar /app/snapshot01.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/snapshot01.jar"]
