FROM maven:3.6-jdk-11 as build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11
WORKDIR /app
COPY --from=build /app/target/app-latest.jar /app/app-latest.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app-latest.jar"]
