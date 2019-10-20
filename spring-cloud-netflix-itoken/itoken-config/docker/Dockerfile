FROM openjdk:8-jre

MAINTAINER Dennis <dennis_lsy@163.com>

RUN mkdir /app

COPY aitoken-config-1.0.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urendom", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]

EXPOSE 8888

