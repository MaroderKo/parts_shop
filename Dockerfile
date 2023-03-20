FROM gradle:7.6.1-jdk17-alpine
#ARG JAR_FILE=./build/libs/*SNAPSHOT.jar
#COPY ${JAR_FILE} application.jar
COPY ./ ./
RUN gradle clean build -x test
ENTRYPOINT ["gradle", "test"]