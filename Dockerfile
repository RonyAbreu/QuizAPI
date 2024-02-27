FROM maven:3.9.6-amazoncorretto-21
WORKDIR /app
COPY . .
RUN mvn package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "/app/target/QuizAPI.jar"]