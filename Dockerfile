FROM openjdk:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/ttt-be-0.0.1-SNAPSHOT-standalone.jar /ttt-be/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/ttt-be/app.jar"]
