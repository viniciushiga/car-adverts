package controllers

import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json._
import play.api.test.Injecting

class CarsControllerCreateSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "POST /cars" should {
    "creates successfully a new car" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(true),
        "mileage" -> JsNull,
        "firstRegistration" -> JsNull
      ))

      val request = FakeRequest(POST, "/cars").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe CREATED
      val json = contentAsJson(create)
      ( json \ "id" ).as[String] must not be empty
      ( json \ "title" ).as[String] must equal("Mercedes-Benz A250")
      ( json \ "price" ).as[Int] must equal(2000000)
      ( json \ "fuel" ).as[String] must equal("Gasoline")
      ( json \ "new" ).as[Boolean] must be (true)
      ( json \ "mileage" ).get must be(JsNull)
      ( json \ "firstRegistration" ).get must be(JsNull)
    }

    "creates successfully an used car" in {
      val body = JsObject(Seq(
        "title" -> JsString("Mercedes-Benz A250"),
        "price" -> JsNumber(2000000),
        "fuel" -> JsString("Gasoline"),
        "new" -> JsBoolean(false),
        "mileage" -> JsNumber(15000),
        "firstRegistration" -> JsString("2018-12-31")
      ))

      val request = FakeRequest(POST, "/cars").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe CREATED
      val json = contentAsJson(create)
      ( json \ "id" ).as[String] must not be(empty)
      ( json \ "title" ).as[String] must equal("Mercedes-Benz A250")
      ( json \ "price" ).as[Int] must equal(2000000)
      ( json \ "fuel" ).as[String] must equal("Gasoline")
      ( json \ "new" ).as[Boolean] must be (false)
      ( json \ "mileage" ).as[Int] must equal(15000)
      ( json \ "firstRegistration").as[String] must equal("2018-12-31")
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

      val request = FakeRequest(POST, "/cars").withJsonBody(body)
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

      val request = FakeRequest(POST, "/cars").withJsonBody(body)
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

      val request = FakeRequest(POST, "/cars").withJsonBody(body)
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

      val request = FakeRequest(POST, "/cars").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe BAD_REQUEST
    }
  }
}
