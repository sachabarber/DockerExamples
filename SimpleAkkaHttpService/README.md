# Docker Demos

I have started this project using the work found here : [Akka HTTP Docker example](https://github.com/vyshane/akka-http-hello-world)
which creates a small Akka Http service hosted in Docker.

## Building and Running the Application

Build the application:


Change to the directory
```
sbt
```
When in SBT console 
```
assembly
```
exit


## Running it in Docker (on Windows)

Opened powershell   

```
docker-compose up
```
from directory. I started to see errors like this
   
```   
   WindowsError: [Error 3] The system cannot find the path specified: 'C:\\Users\\sacha\\Downloads\\akka-http-hello-world-m
   aster\\akka-http-hello-world-master\\target\\streams\\$global\\assemblyOption\\$global\\streams\\assembly\\0c81e49ff9131
   8ea4538d8c1f450394783f174fa_d0f1d04d9e2bf10f3d5102fdc325cbae1bad6aad\\akka\\stream\\AbruptTerminationException$.class'
```

Did some googling this seems to be about the long file names that can occur on Windows. So I just copied the entire set of files/src/target etc etc to
C:\X folder ignoring any long file names that it said were bad. I simply ignored them
   


   
Change to the directory
```
C:\X 
```   
   
Then run   
```
docker-compose up --build 
```


NOTE : If container was already running using docker-ps, grab its Id, then docker rm --force <ID>



Use postman to look at http://localhost:8080
   
   
   






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