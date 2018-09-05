#!/bin/sh
echo "Demarrage du composant object"
java -Dspring.config.location="/application.yml" -Dserver.port=9001 -jar /object.jar

