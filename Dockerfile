FROM java

COPY ./build/libs/*.jar app.jar

EXPOSE ${SERVER_PORT:-8080}
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]