FROM openjdk:17
COPY target/todo-backend-*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]