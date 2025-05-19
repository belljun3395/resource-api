FROM  openjdk:18-oracle

RUN mkdir -p /logs

ENV	PROFILE=default
ENV TZ=Asia/Seoul
EXPOSE 8080

ARG JAVA_OPTS

ARG RELEASE_VERSION
ENV DD_VERSION=${RELEASE_VERSION}

ARG JAR_FILE="build/libs/resource-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dkotlinx.coroutines.debug=on", "-jar", "app.jar"]
