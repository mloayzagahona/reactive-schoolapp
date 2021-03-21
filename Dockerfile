FROM adoptopenjdk/openjdk11:alpine-jre

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/schoolapp/app.jar
LABEL email="mloayzagahona@gmail.com" author="Manuel Loayza"
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/usr/share/schoolapp/app.jar"]
