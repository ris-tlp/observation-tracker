FROM maven:3.9.6-eclipse-temurin-17 as MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/observation-tracker-RELEASE.jar /app/
ENTRYPOINT ["java", "-jar","/app/observation-tracker-RELEASE.jar"]
