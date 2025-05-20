# Etapa 1: construir el proyecto
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagen final, más liviana
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/clinica-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]