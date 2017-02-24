# Docker Demos

I have started this project using the work found here : [Akka HTTP Docker example](https://github.com/vyshane/akka-http-hello-world)
which creates a small Akka Http service hosted in Docker.


Cassandra Network check

Assuming you have a cassandra container running already, Get the networks to use with --net switch

```docker network ls```


Then use the name of the cassandra containers network

```docker run -it --link cassandra-1:cassandra --rm cassandra cqlsh cassandra --net simpleakkahttpservicewithcassandra_default```



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
docker-compose up --build
```
from directory. I started to see errors like this
   
```   
   WindowsError: [Error 3] The system cannot find the path specified: 'C:\\Users\\sacha\\Downloads\\akka-http-hello-world-m
   aster\\akka-http-hello-world-master\\target\\streams\\$global\\assemblyOption\\$global\\streams\\assembly\\0c81e49ff9131
   8ea4538d8c1f450394783f174fa_d0f1d04d9e2bf10f3d5102fdc325cbae1bad6aad\\akka\\stream\\AbruptTerminationException$.class'
```

Did some googling this seems to be about the long file names that can occur on Windows. 

So I then did this

- Create new directory on C:\ for me this was C:\X

So I just copied the following files to C:\X

- docker-compose.yml
- SimpleAkkaHttpServiceWithCassandra.jar
- dockerfile

   
Change to the directory
```
C:\X 
```   
   
Then run   
```
docker-compose up --build 
```


NOTE : If container was already running using docker-ps, grab its Id, then docker rm  <ID> --force


## Testing It

Used postman to look at http://localhost:8080
   
   

## Useful Links

* [Akka HTTP - The What, Why and How](https://www.youtube.com/watch?v=y_slPbktLr0) (Video)
* [Akka HTTP documentation](http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC4/scala/http/)
* [Akka HTTP Microservice Example](https://www.typesafe.com/activator/template/akka-http-microservice) (Tutorial with code)
* [Docker documentation](https://docs.docker.com/)