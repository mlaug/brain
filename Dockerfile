FROM java:8

RUN mkdir -p /usr/app
RUN mkdir -p /usr/build
COPY ./ /usr/build
RUN cd /usr/build && ./gradlew build && cp /usr/build/build/libs/brain-0.1.0.jar /usr/app/app.jar

WORKDIR /usr/app
CMD ["java", "-jar", "app.jar"]