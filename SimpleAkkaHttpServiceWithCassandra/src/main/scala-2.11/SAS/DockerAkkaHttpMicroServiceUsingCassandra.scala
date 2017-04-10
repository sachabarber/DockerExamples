package SAS

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.datastax.driver.core.{Session, ResultSet, Cluster}
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
      var returnVal="getSomeCassandraData\r\n"
      try {
        cluster = Cluster.builder()
          .addContactPoint("cassandra-1")
          .withPort(9042)
          .build()
        val session = cluster.connect()


        returnVal = returnVal + "Before keyspace\r\n"
        createKeySpace(session)
        returnVal = returnVal + "After keyspace\r\n"

        createTable(session)
        returnVal = returnVal + "After table\r\n"

        seedData(session)
        returnVal = returnVal + "After seeding\r\n"

        returnVal = returnVal + "\r\n"
        val rs = session.execute("select category, points, lastname from dockercassandra.cyclist_category")

        val iter = rs.iterator()
        while (iter.hasNext()) {
          if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched())
            rs.fetchMoreResults()
          val row = iter.next()
            returnVal = returnVal + s"category= ${row.getString("category")}, " +
              s"points= ${row.getInt("points")}, lastname= ${row.getString("lastname")}\r\n"

        }
      }
      catch {
        case e: Exception =&gt; {
          e.printStackTrace
          returnVal = e.printStackTrace().toString
        }
      } finally {
        if (cluster != null) cluster.close()
      }
      returnVal
    }

    def createKeySpace(session : Session) : Unit = {
      session.execute(
        """CREATE KEYSPACE IF NOT EXISTS DockerCassandra
            WITH replication = {
                 'class':'SimpleStrategy',
                 'replication_factor' : 3
               };""")
    }


    def createTable(session : Session) : Unit = {
      session.execute(
        """CREATE TABLE IF NOT EXISTS dockercassandra.cyclist_category (
             category text,
             points int,
             lastname text,
             PRIMARY KEY (category, points))
          WITH CLUSTERING ORDER BY (points DESC);""")
    }

    def seedData(session : Session) : Unit = {
      session.execute("TRUNCATE dockercassandra.cyclist_category;")
      session.execute("INSERT INTO dockercassandra.cyclist_category (category, points, lastname) VALUES ('cat1', 15, 'barber');");
      session.execute("INSERT INTO dockercassandra.cyclist_category (category, points, lastname) VALUES ('cat2', 25, 'smith');");
      session.execute("INSERT INTO dockercassandra.cyclist_category (category, points, lastname) VALUES ('cat3', 45, 'evans');");
      session.execute("INSERT INTO dockercassandra.cyclist_category (category, points, lastname) VALUES ('cat4', 65, 'harth');");
    }

    val routes =
      get {
        pathSingleSlash {
          complete {
            getSomeCassandraData()
            //"getting cass"
          }
        }
      }

    Http().bindAndHandle(routes,
      config.getString("http.address"),
      config.getInt("http.port"))

  }
}