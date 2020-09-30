FROM maven:3.6-jdk-11 as build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11
WORKDIR /app
COPY --from=build /app/target/com.felipekzig.widget.jar /app/com.felipekzig.widget.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/com.felipekzig.widget.jar"]
