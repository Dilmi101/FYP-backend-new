FROM eclipse-temurin:17-jdk-alpine
COPY target/*.jar ehb.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/ehb.jar"]