FROM amazoncorretto:8-alpine3.15-jre

COPY target/carAds* /opt/carads/carads.jar
WORKDIR /opt/carads

#CMD ["java","-jar","carads.jar"]
ENTRYPOINT ["tail", "-f", "/dev/null"]