FROM java:8
RUN mkdir /app
ADD ./build/libs/brain-0.1.0.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]