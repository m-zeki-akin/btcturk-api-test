# syntax=docker/dockerfile:1

FROM openjdk:17.0.1-jdk-slim as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as test
CMD ["./mvnw", "clean", "test"]