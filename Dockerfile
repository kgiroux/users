FROM openjdk:8-jdk-alpine
MAINTAINER Kevin Giroux <giroux.kevin.fr@gmail.com>

ADD object.jar /object.jar
ADD entrypoint.sh /entrypoint.sh
ADD application.yml /application.yml

RUN mkdir -p /home/dofustuff/media
VOLUME ["/var/log/object.log", "/home/dofustuff/media"]

ENTRYPOINT ["./entrypoint.sh"]
