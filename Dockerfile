FROM openjdk:16-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://bank_base:5432/bank_base
ENV SPRING_DATASOURCE_PASSWORD=bank_pwd
ENTRYPOINT ["java","-jar","/app.jar"]