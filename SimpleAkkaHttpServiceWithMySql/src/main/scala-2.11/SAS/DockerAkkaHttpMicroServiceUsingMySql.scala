package SAS

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import java.sql.{ResultSet, Connection, DriverManager}


object DockerAkkaHttpMicroServiceUsingMySql {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val logger = Logging(system, getClass)


    def getSomeMySqlData() : String = {
      // connect to the database named "mysql" on port 3306 of localhost
      val url = "jdbc:mysql://db:3306/mysql"
      val driver = "com.mysql.jdbc.Driver"
      val username = "root"
      val password = "sacha"
      var connection:Connection = null
      var returnVal=""
      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement
        val rs = statement.executeQuery("SELECT table_name, table_schema FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE';")

        realize(rs).take(10).foreach(v=>
        {
          val valuesList = v.values.toList
          val tableName = valuesList(0)
          val tableSchema = valuesList(1)
          returnVal = returnVal + s"tableName= $tableName, tableSchema= $tableSchema\r\n"
        })
      } catch {
        case e: Exception => e.printStackTrace
      }
      connection.close
      returnVal
    }


    def buildMap(queryResult: ResultSet,
                 colNames: Seq[String]): Option[Map[String, Object]] =
      if (queryResult.next())
        Some(colNames.map(n => n -> queryResult.getObject(n)).toMap)
      else
        None

    def realize(queryResult: ResultSet): Vector[Map[String, Object]] = {
      val md = queryResult.getMetaData
      val colNames = (1 to md.getColumnCount) map md.getColumnName
      Iterator.continually(buildMap(queryResult, colNames))
        .takeWhile(!_.isEmpty).map(_.get).toVector
    }

    val routes =
      get {
        pathSingleSlash {
          complete {
            getSomeMySqlData()
          }
        }
      }

    Http().bindAndHandle(routes,
      config.getString("http.address"),
      config.getInt("http.port"))

  }
}
