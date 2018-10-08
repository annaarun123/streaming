# play-scala-starter-example

[<img src="https://img.shields.io/travis/playframework/play-scala-starter-example.svg"/>](https://travis-ci.org/playframework/play-scala-starter-example)

This is a starter application that shows how Play works.  Please see the documentation at <https://www.playframework.com/documentation/latest/Home> for more details.

## Running

Run this using [sbt](http://www.scala-sbt.org/).  If you downloaded this project from <http://www.playframework.com/download> then you'll find a prepackaged version of sbt in the project directory:

```bash
sbt run
```

And then go to <http://localhost:9000> to see the running web application.

There are several demonstration files available in this template.

## Controllers

- HomeController.scala:

  Shows how to handle simple HTTP requests.

- AsyncController.scala:

  Shows how to do asynchronous programming when handling a request.

- KafkaStreamController.scala:

  Shows how to stream messages from a kafka topic continously in http using kafka-stream.
