FROM mozilla/sbt
WORKDIR /build
ADD . .

EXPOSE 9000
RUN ["sbt", "update"]
