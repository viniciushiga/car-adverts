package db

import java.util.UUID

import akka.actor.ActorSystem
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import com.gu.scanamo.syntax._
import com.gu.scanamo.{ScanamoAsync, Table}
import db.DynamoFormats._
import javax.inject.{Inject, Singleton}
import models.Car
import play.api.Configuration

import scala.concurrent.Future

@Singleton
class CarsRepository @Inject() (config: Configuration, client: AmazonDynamoDBAsync, system: ActorSystem) {
  implicit val context = system.dispatchers.lookup("car-repository")

  val table = Table[Car](config.get[String]("dynamodb.table-name"))

  def findAll(): Future[Seq[Car]] = {
    val ops = for {
      cars <- table.scan()
    } yield cars

    ScanamoAsync.exec(client)(ops).map(list => {
      list.collect {
        case Right(car) => car
      }
    })
  }

  def find(id: UUID): Future[Option[Car]] = {
    val ops = for {
      result <- table.get('id -> id)
    } yield result

    ScanamoAsync.exec(client)(ops).map(_.flatMap(_.toOption))
  }

  def create(car: Car): Future[Option[Car]] = {
    val ops = for {
      _ <- table.put(car)
      result <- table.get('id -> car.id)
    } yield result

    ScanamoAsync.exec(client)(ops).map(_.flatMap(_.toOption))
  }

  def update(car: Car): Future[Option[Car]] = {
    find(car.id).flatMap(_.map(_ => create(car)).getOrElse(Future.successful(None)))
  }

  def delete(id: UUID): Future[Option[UUID]] = {
    find(id).flatMap({
      _.map(c => {
        val ops = for {
          result <- table.delete('id -> c.id)
        } yield result

        ScanamoAsync.exec(client)(ops).map(_ => Some(c.id))
      }).getOrElse(Future.successful(None))
    })
  }
}
