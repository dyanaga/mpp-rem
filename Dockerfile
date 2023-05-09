# docker buildx build --platform linux/amd64,linux/arm64/v8,linux/amd64 -t docker.olahistvan.com/bachelor/pos:<version> --push .
FROM openjdk:17-jdk-oraclelinux8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]