FROM java:8
VOLUME /tmp
ADD . .
RUN bash -c 'touch /gradlew'
ENTRYPOINT ["/bin/bash","/gradlew","clean","tomcatStartWar"]