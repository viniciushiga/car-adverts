package controllers

import java.util.UUID

import db.CarsRepository
import models.{Car, Fuel}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, _}

import scala.concurrent.duration._
import scala.concurrent.Await

class CarsControllerIndexSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val context = scala.concurrent.ExecutionContext.global

  "GET /cars" should {
    "returns all cars" in {
      val repo = inject[CarsRepository]
      val car = Car(UUID.randomUUID(), "BMW 120i", Fuel.Gasoline, 1000000, true, None, None)
      Await.result(repo.create(car), 5.seconds)

      val request = FakeRequest(GET, "/cars")
      val index = route(app, request).get

      status(index) mustBe OK
      val json = contentAsJson(index)
      ( json \\ "title" ).map(_.as[String]) must contain("BMW 120i")
    }
  }
}
