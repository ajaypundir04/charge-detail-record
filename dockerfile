FROM openjdk:22-jdk
VOLUME /tmp
ADD target/charge-detail-record*SNAPSHOT.jar charge-detail-record.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/charge-detail-record.jar"]