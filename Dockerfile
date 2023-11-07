# syntax=docker/dockerfile:1

FROM openjdk:17.0.1-jdk-slim
CMD ./gradlew bootJar
COPY ./build/libs /app
#CMD /app/gradlew bootJar
CMD java -jar /app/shop-0.0.1-SNAPSHOT.jar --spring.profiles.active=Container