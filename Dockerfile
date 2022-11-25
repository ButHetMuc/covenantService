FROM adoptopenjdk/openjdk16:alpine

COPY target/rent-0.0.1-SNAPSHOT.jar rent-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/rent-0.0.1-SNAPSHOT.jar"]