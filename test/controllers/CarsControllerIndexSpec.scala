package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, _}

class CarsControllerIndexSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "GET /cars" should {
    "renders all cars" in {
      val request = FakeRequest(GET, "/cars")
      val index = route(app, request).get

      status(index) mustBe OK
      val json = contentAsJson(index)
      ( json \\ "title" ).map(_.as[String]) must equal(Seq("Audi A3", "BMW 120d"))
    }
  }
}
