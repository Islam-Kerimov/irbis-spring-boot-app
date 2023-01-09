FROM openjdk:17-jdk-slim
#FROM eclipse-temurin:17-jre-alpine
ADD target/irbis-web-app-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]