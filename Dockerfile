FROM azul/zulu-openjdk-alpine:21-jre-headless-latest

MAINTAINER kokyeomjae<kosk6154@naver.com>

ARG MODULE_NAME
ARG JAR_PATH=$MODULE_NAME/build/libs/$MODULE_NAME.jar
ARG JAR_NAME=$MODULE_NAME.jar

ENV JAR_PATH $JAR_PATH
ENV JAR_NAME $JAR_NAME

COPY $JAR_PATH $JAR_NAME

RUN apk add -U tzdata
RUN apk --no-cache add tzdata && \
        cp /usr/share/zoneinfo/UTC /etc/localtime && \
        echo "UTC" > /etc/timezone

COPY $JAR_PATH $JAR_NAME

EXPOSE 8080

ENTRYPOINT java \
#    -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
    -Djava.net.preferIPv4Stack=true \
    -Dserver.port=8080 \
    -jar $JAR_NAME

