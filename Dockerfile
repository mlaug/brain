FROM java:8
RUN mkdir -p /usr/app
ADD ./build/libs/brain-0.1.0.jar /usr/app/app.jar
WORKDIR /usr/app
CMD ["java", "-jar", "app.jar"]