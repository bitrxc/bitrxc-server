# Build
FROM maven AS build-env
WORKDIR /usr/src/app

COPY . .
RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:go-offline
RUN mvn -B -e clean package

# Package
FROM openjdk:8
COPY --from=build-env /usr/src/app/target/*.jar ./app.jar

ENV JAVA_OPTS=""
ENV SERVER_PORT 8080

EXPOSE ${SERVER_PORT}

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar /app.jar" ]