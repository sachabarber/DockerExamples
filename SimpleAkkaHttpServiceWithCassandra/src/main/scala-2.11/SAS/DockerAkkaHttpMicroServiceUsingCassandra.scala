package SAS

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.datastax.driver.core.{ResultSet, Cluster}
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._


object DockerAkkaHttpMicroServiceUsingCassandra {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val logger = Logging(system, getClass)

    def getSomeCassandraData() : String = {

      var cluster:Cluster = null
      var returnVal=""
      try {
        cluster = Cluster.builder()
          .addContactPoint("cassandra-1")
          .build()
        val session = cluster.connect()

        val rs = session.execute("select cluster_name, release_version from system.local")
        realize(rs).take(10).foreach(v =>
        {
          val valuesList = v.values.toList
          val cluster_name = valuesList(0)
          val release_version = valuesList(1)
          returnVal = returnVal + s"cluster_name= $cluster_name, release_version= $release_version\r\n"
        })

      }
      catch {
        case e: Exception => e.printStackTrace
      } finally {
        if (cluster != null) cluster.close()
      }
      returnVal
    }

    def buildMap(queryResult: ResultSet, colNames: Seq[String]): Option[Map[String, Object]] = {
      val iter = queryResult.iterator()
      if (iter.hasNext())
        Some(colNames.map(n => n -> iter.next().getObject(n)).toMap)
      else
        None
    }

    def realize(queryResult: com.datastax.driver.core.ResultSet): Vector[Map[String, Object]] = {
      val colNames = queryResult.getColumnDefinitions.asList().map(x => x.getName)
      Iterator.continually(buildMap(queryResult, colNames))
              .takeWhile(!_.isEmpty).map(_.get).toVector
    }

    val routes =
      get {
        pathSingleSlash {
          complete {
            getSomeCassandraData()
          }
        }
      }

    Http().bindAndHandle(routes,
      config.getString("http.address"),
      config.getInt("http.port"))

  }
}
