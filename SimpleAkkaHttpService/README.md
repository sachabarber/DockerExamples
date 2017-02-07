# Docker Demos

I have started this project using the work found here : [Akka HTTP Docker example](https://github.com/vyshane/akka-http-hello-world)
which creates a small Akka Http service hosted in Docker.

## Building and Running the Application

Build the application:

```bash
$ sbt assembly
```

Launch in a Docker container. You need to have Docker and Docker Compose installed.

```bash
$ docker-compose up
```

Send a test request, where xxx.xxx.xxx.xxx is the IP address of the Docker container:

```bash
$ curl http://xxx.xxx.xxx.xxx
Hello, World!
```

## Useful Links

* [Akka HTTP - The What, Why and How](https://www.youtube.com/watch?v=y_slPbktLr0) (Video)
* [Akka HTTP documentation](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC4/scala/http/)
* [Akka HTTP Microservice Example](https://www.typesafe.com/activator/template/akka-http-microservice) (Tutorial with code)
* [Docker documentation](https://docs.docker.com/)