FROM java:8

RUN curl -sL https://deb.nodesource.com/setup_7.x | bash -
RUN apt-get update && apt-get install -y nodejs && apt-get install -y  --no-install-recommends maven

RUN java -version
RUN mvn --version

COPY pom.xml /build/pom.xml
COPY explorer-model /build/explorer-model
COPY explorer-dao /build/explorer-dao
COPY explorer-service /build/explorer-service
COPY explorer-web /build/explorer-web

RUN cd /build && mvn clean compile

COPY explorer-web-ui /build/explorer-web-ui
RUN cd /build/explorer-web-ui && npm install && npm run build && cd /build

RUN mkdir -p /build/explorer-web/target/classes/static/static/css
RUN mkdir -p /build/explorer-web/target/classes/static/static/favicons
RUN mkdir -p /build/explorer-web/target/classes/static/static/js
RUN mkdir -p /build/explorer-web/target/classes/static/static/media

RUN cp -r /build/explorer-web-ui/build/static/css /build/explorer-web/target/classes/static/static/
RUN cp -r /build/explorer-web-ui/build/favicon.ico /build/explorer-web/target/classes/static/static/favicons/favicon.ico
RUN cp -r /build/explorer-web-ui/build/static/js /build/explorer-web/target/classes/static/static/
RUN cp -r /build/explorer-web-ui/build/static/media /build/explorer-web/target/classes/static/static/

RUN cp /build/explorer-web-ui/build/index.html /build/explorer-web/target/classes/templates/index.html

RUN ls -alhR /build/explorer-web/target/classes/static

RUN cd /build && mvn package && cd /build

RUN cp /build/explorer-web/target/explorer-web-0.0.1-SNAPSHOT.jar /app.jar
RUN rm -rf /build

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

WORKDIR /
