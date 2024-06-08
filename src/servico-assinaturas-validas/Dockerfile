# build
FROM eclipse-temurin:21-jdk-alpine AS build
RUN apk add --no-cache maven
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN mvn clean package

# implantacao
FROM eclipse-temurin:21-jre-alpine AS prod
WORKDIR /workspace/app
COPY --from=build /workspace/app/target/*.jar /workspace/app/app.jar
EXPOSE 8101
ENTRYPOINT ["java", "-jar", "/workspace/app/app.jar"]