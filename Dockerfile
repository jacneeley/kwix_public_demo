FROM maven:3.9.9-ibm-semeru-21-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src/ ./src/
RUN mvn clean package -DskipTests

FROM openjdk:24-jdk-slim
WORKDIR /app

COPY --from=build /app/target/simplepayroll-0.0.1-SNAPSHOT.jar simplepayroll.jar


EXPOSE 8042
ENTRYPOINT ["java", "-jar", "simplepayroll.jar"]