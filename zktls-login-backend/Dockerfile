FROM eclipse-temurin:21.0.7_6-jre-noble
VOLUME /tmp
COPY build/libs/zktls-login-backend-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]