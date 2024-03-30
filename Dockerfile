FROM openjdk:17-jdk-slim
COPY target/*.jar ehb.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/ehb.jar"]
