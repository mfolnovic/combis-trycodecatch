FROM java:8

RUN aptget npm
RUN aptget mvn

RUN cd team-three-web && mvn clean compile && cd ..
RUN cd team-three-web-ui && npm install && npm run build && cd ..

RUN cp -r ./team-three-web-ui/build/css ./team-three-web/target/classes/static/css
RUN cp -r ./team-three-web-ui/build/favicons ./team-three-web/target/classes/static/favicons
RUN cp -r ./team-three-web-ui/build/js ./team-three-web/target/classes/static/js
RUN cp -r ./team-three-web-ui/build/media ./team-three-web/target/classes/static/media

RUN cp ./team-three-web-ui/build/index.html ./team-three-web/target/classes/templates/index.html

RUN cd team-three-web && mvn package && cd ..

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/stemi/app.jar"]

WORKDIR /home/team-three

ADD team-three-web-0.0.1-SNAPSHOT.jar /home/team-three/app.jar
RUN sh -c 'touch /home/team-three/app.jar'
