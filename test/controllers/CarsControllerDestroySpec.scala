package controllers

import java.util.UUID

import db.CarsRepository
import models.{Car, Fuel}
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

import scala.concurrent.duration._
import scala.concurrent.Await

class CarsControllerDestroySpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val context = scala.concurrent.ExecutionContext.global

  "DELETE /cars/:id" should {
    "deletes car when it exits" in {
      val repo = inject[CarsRepository]
      val car = Car(UUID.randomUUID(), "Mercedes-Benz A250", Fuel.Gasoline, 2000000, true, None, None)
      Await.result(repo.create(car), 5.seconds)

      val id = car.id.toString
      val request = FakeRequest(DELETE, s"/cars/$id")
      val show = route(app, request).get

      status(show) mustBe NO_CONTENT
      Await.result(repo.find(car.id), 5.seconds) mustBe empty
    }

    "returns 404 car when does not exit" in {
      val id = "00000000-0000-0000-0000-000000000000"
      val request = FakeRequest(DELETE, s"/cars/$id")
      val show = route(app, request).get

      status(show) mustBe NOT_FOUND
    }
  }
}
