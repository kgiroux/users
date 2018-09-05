    #!/bin/sh
echo "Demarrage du composant d'acquisition"
java -Xms2048m -Xmx6144m -Djava.security.egd=file:/dev/./urandom  -Dspring.config.location="/application.yml,/rabbit.yml" -Dserver.port=9001 -jar /acquisition.jar

