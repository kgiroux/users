FROM openjdk:8-jdk-alpine
MAINTAINER Kevin Giroux <giroux.kevin.fr@gmail.com>

ADD acquisition.jar /acquisition.jar
ADD entrypoint.sh /entrypoint.sh
ADD assets/config/timezone/Paris /etc/localtime

VOLUME ["/var/log/object.log"]

ENTRYPOINT ["./entrypoint.sh"]
