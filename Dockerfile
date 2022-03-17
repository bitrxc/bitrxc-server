# Build
FROM maven:3.8.4-openjdk-8 AS build-env
WORKDIR /usr/src/app

COPY service/pom.xml service/pom.xml
COPY service-base/pom.xml service-base/pom.xml
COPY pom.xml settings.xml ./
RUN mvn -B -e -s settings.xml dependency:go-offline
COPY . .
RUN mvn -B -e -s settings.xml clean package -DskipTests

# Package
FROM openjdk:8u322 AS serve-env
WORKDIR /app

COPY deploy/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
COPY --from=build-env /usr/src/app/service/target/*.jar /app/app.jar

# -Djava.security.egd=file:/dev/urandom
#     Accelerate the processs of generating secure random number for variouse Java libraries
# -Duser.timezone=Asia/Shanghai
#     Change the system timezone to Asia/Shanghai
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/urandom -Duser.timezone=Asia/Shanghai"
ENV SERVER_PORT 8080

# Keep target port identical with port in application.yaml
EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar" ]