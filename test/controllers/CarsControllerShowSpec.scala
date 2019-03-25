package controllers

import java.util.UUID

import db.CarsRepository
import models.{Car, Fuel}
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.JsNull
import play.api.test.Injecting

import scala.concurrent.Await
import scala.concurrent.duration._

class CarsControllerShowSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val context = scala.concurrent.ExecutionContext.global

  "GET /cars/:id" should {
    "returns car when it exits" in {
      val repo = inject[CarsRepository]
      val car = Car(UUID.randomUUID(), "Mercedes-Benz A250", Fuel.Gasoline, 2000000, true, None, None)
      Await.result(repo.create(car), 5.seconds)

      val id = car.id.toString
      val request = FakeRequest(GET, s"/cars/$id")
      val show = route(app, request).get

      status(show) mustBe OK
      val json = contentAsJson(show)
      ( json \ "id" ).as[String] must equal(id)
      ( json \ "title" ).as[String] must equal("Mercedes-Benz A250")
      ( json \ "price" ).as[Int] must equal(2000000)
      ( json \ "fuel" ).as[String] must equal("Gasoline")
      ( json \ "new" ).as[Boolean] must be (true)
      ( json \ "mileage" ).get must be(JsNull)
      ( json \ "firstRegistration" ).get must be(JsNull)
    }

    "returns 404 when it does not exist" in {
      val id = "00000000-0000-0000-0000-000000000000"
      val request = FakeRequest(GET, s"/cars/$id")
      val show = route(app, request).get

      status(show) mustBe NOT_FOUND
    }
  }
}
