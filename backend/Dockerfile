FROM openjdk:17-jdk-slim
WORKDIR /app
COPY ./mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
COPY src src
CMD ["./mvnw", "spring-boot:run"]