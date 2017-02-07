FROM hseeberger/scala-sbt
MAINTAINER sacha barber <sacha.barber@gmail.com>
ENV REFRESHED_AT 2017-07-02

ADD . /root
WORKDIR /root

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/scala-2.11/SimpleAkkaHttpService.jar"]