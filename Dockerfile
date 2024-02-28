FROM amazoncorretto:21.0.2-alpine3.19
WORKDIR /app
ARG JAR_FILE=target/QuizAPI.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]