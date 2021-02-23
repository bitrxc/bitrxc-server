# Build
FROM maven:3 AS build-env
WORKDIR /usr/src/app

COPY service/pom.xml service/pom.xml
COPY service-base/pom.xml service-base/pom.xml
COPY pom.xml settings.xml ./
RUN mvn -B -e -s settings.xml dependency:go-offline
COPY . .
RUN mvn -B -e -s settings.xml clean package -DskipTests

# Package
FROM openjdk:8 AS package-env
COPY --from=build-env /usr/src/app/service/target/*.jar ./app.jar

ENV JAVA_OPTS=""
ENV SERVER_PORT 8080

EXPOSE ${SERVER_PORT}

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar /app.jar" ]