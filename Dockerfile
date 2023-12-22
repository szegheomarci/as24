FROM amazoncorretto:8-alpine3.15-jre

# install yq
RUN apk add yq

COPY target/carAds* /carads.jar
COPY *.sh /

#CMD ["java","-jar","carads.jar"]
#ENTRYPOINT ["tail", "-f", "/dev/null"]
CMD ["./start.sh"]
