FROM openjdk:17-jdk-slim
WORKDIR /app
EXPOSE 8080
COPY target/observation-tracker-RELEASE.jar /app/observation-tracker-RELEASE.jar
#ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar","/app/observation-tracker-RELEASE.jar"]
ENTRYPOINT ["java", "-jar","/app/observation-tracker-RELEASE.jar"]