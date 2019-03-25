package controllers

import java.time.LocalDate
import java.util.UUID

import db.CarsRepository
import models.{Car, Fuel}
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json._
import play.api.test.Helpers.{POST, contentAsJson, route, status}
import play.api.test.{FakeRequest, Injecting}

import scala.concurrent.duration._
import scala.concurrent.Await

class CarsControllerUpdateSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val context = scala.concurrent.ExecutionContext.global

  "PUT /cars/:id" should {
    "updates successfully a new car" in {
      val repo = inject[CarsRepository]
      val car = Car(UUID.randomUUID(), "BMW 120i", Fuel.Gasoline, 1000000, true, None, None)
      Await.result(repo.create(car), 5.seconds)

      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(true),
        "mileage" -> JsNull,
        "firstRegistration" -> JsNull
      ))

      val id = car.id.toString
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe OK
      val json = contentAsJson(create)
      ( json \ "id" ).as[String] must equal(id)
      ( json \ "title" ).as[String] must equal("Mercedes-Benz A250")
      ( json \ "price" ).as[Int] must equal(2000000)
      ( json \ "fuel" ).as[String] must equal("Gasoline")
      ( json \ "new" ).as[Boolean] must be (true)
      ( json \ "mileage" ).get must be(JsNull)
      ( json \ "firstRegistration" ).get must be(JsNull)

      val expected = Car(car.id, "Mercedes-Benz A250", Fuel.Gasoline, 2000000, true, None, None)
      Await.result(repo.find(car.id), 5.seconds) must equal(Some(expected))
    }

    "updates successfully an used car" in {
      val repo = inject[CarsRepository]
      val car = Car(UUID.randomUUID(), "BMW 120i", Fuel.Gasoline, 1000000, false, Some(10000), Some(LocalDate.of(2017, 1, 1)))
      Await.result(repo.create(car), 5.seconds)

      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(false),
        "mileage" -> JsNumber(15000),
        "firstRegistration" -> JsString("2018-12-31")
      ))

      val id = car.id.toString
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe OK
      val json = contentAsJson(create)
      ( json \ "id" ).as[String] must not be empty
      ( json \ "title" ).as[String] must equal("Mercedes-Benz A250")
      ( json \ "price" ).as[Int] must equal(2000000)
      ( json \ "fuel" ).as[String] must equal("Gasoline")
      ( json \ "new" ).as[Boolean] must be (false)
      ( json \ "mileage" ).as[Int] must equal(15000)
      ( json \ "firstRegistration").as[String] must equal("2018-12-31")

      val expected = Car(car.id, "Mercedes-Benz A250", Fuel.Gasoline, 2000000, false, Some(15000), Some(LocalDate.of(2018, 12, 31)))
      Await.result(repo.find(car.id), 5.seconds) must equal(Some(expected))
    }

    "returns 404 when car does not exist" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(false),
        "mileage" -> JsNumber(15000),
        "firstRegistration" -> JsString("2018-12-31")
      ))

      val id = UUID.randomUUID().toString
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe NOT_FOUND
    }

    "returns 400 when used car does not have mileage" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(false),
        "mileage" -> JsNull,
        "firstRegistration" -> JsString("2018-12-31")
      ))

      val id = UUID.randomUUID()
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe BAD_REQUEST
    }

    "returns 400 when used car does not have firstRegistration" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(false),
        "mileage" -> JsNumber(15000),
        "firstRegistration" -> JsNull
      ))

      val id = UUID.randomUUID()
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe BAD_REQUEST
    }

    "returns 400 when new car has mileage" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(true),
        "mileage" -> JsNumber(15000),
        "firstRegistration" -> JsNull
      ))

      val id = UUID.randomUUID()
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe BAD_REQUEST
    }

    "returns 400 when new car has firstRegistration" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(true),
        "mileage" -> JsNull,
        "firstRegistration" -> JsString("2018-12-31")
      ))

      val id = UUID.randomUUID()
      val request = FakeRequest(PUT, s"/cars/$id").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe BAD_REQUEST
    }
  }
}
