FROM openjdk:11-jre-slim

WORKDIR /myapp
VOLUME /tmp
ADD /target/ekar-machine-problem*jar /myapp/app.jar

ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /myapp/app.jar" ]