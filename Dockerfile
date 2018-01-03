FROM openjdk:8
RUN mkdir /work
RUN mkdir /work/myretail
EXPOSE 8080
COPY ./build/libs/myretail-service*.jar /work/myretail/myretail-service.jar
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/work/myretail/myretail-service.jar"]
