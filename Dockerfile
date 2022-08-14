FROM gradle:7.4-jdk17-alpine AS builder

WORKDIR /build

#gradle 의존성 캐싱
COPY build.gradle /build
RUN gradle build --parallel > /dev/null 2>&1 || true

#프로젝트 빌드
COPY . /build
RUN gradle clean bootjar


FROM openjdk:17

WORKDIR /app

COPY --from=builder /build/build/libs/*.jar .

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "job-finder-0.0.1-SNAPSHOT.jar"]