FROM maven:3.9.6-eclipse-temurin-17 as build
COPY src src
COPY pom.xml pom.xml
RUN mvn clean install

FROM openjdk:17
RUN adduser --system test-user
USER test-user
WORKDIR /app
COPY --from=build target/rps-game-0.0.1-SNAPSHOT.jar ./rps-game.jar
ENTRYPOINT ["java", "-jar", "./rps-game.jar", "-h"]
