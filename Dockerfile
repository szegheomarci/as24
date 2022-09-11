FROM maven:3.8.6 as builder

RUN mkdir -p /home/app
COPY . /home/app
WORKDIR /home/app
RUN mvn compile assembly:single
RUN mkdir -p /home/app/target/jar && mv /home/app/target/carAds* /home/app/target/jar/carads.jar

FROM amazoncorretto:8-alpine3.15-jre

COPY --from=builder /home/app/target/jar /opt/carads/
WORKDIR /opt/carads

CMD ["java","-jar","carads.jar"]