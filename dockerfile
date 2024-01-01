FROM openjdk:11
COPY ./target/user-service-0.0.1-SNAPSHOT.jar application.jar
ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "-Dspring.config.name=bootstrap", "/application.jar"]