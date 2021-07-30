FROM maven:3.5-jdk-8 AS build  
LABEL Diogo Bruno Almeida <diogo.bruno2@gmail.com>
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app
RUN sed -i 's/localhost:3306/mysqlsrv/g' /usr/src/app/src/main/resources/application.properties
RUN cd /usr/src/app && mvn clean package -B

FROM openjdk:8 
COPY --from=build /usr/src/app/target/projectws-1.0.0-SNAPSHOT.jar /usr/app/projectws-1.0.0-SNAPSHOT.jar  
EXPOSE 8010  
ENV JAVA_OPTS ""
CMD java ${JAVA_OPTS} -jar /usr/app/projectws-1.0.0-SNAPSHOT.jar