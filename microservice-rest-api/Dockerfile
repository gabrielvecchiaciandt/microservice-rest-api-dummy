FROM 262087054404.dkr.ecr.sa-east-1.amazonaws.com/golden-image/alpine/openjdk:17

COPY build/libs/microservice-rest-api-*.jar /deployments/app.jar
ENV LANG="en_US.UTF-8"
# Ports exposing
EXPOSE 8080

CMD ["java", "-jar", "/deployments/app.jar"]