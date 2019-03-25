package controllers

import db.CarsRepository
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class CarsControllerIndexSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "GET /cars" should {
    "renders all cars" in {
      val controller = new CarsController(stubControllerComponents(), inject[CarsRepository])
      val index = controller.index().apply(FakeRequest(GET, "/cars"))

      status(index) mustBe OK
      val json = contentAsJson(index)
      ( json \\ "title" ).map(_.as[String]) must equal(Seq("Audi A3", "BMW 120d"))
    }
  }
}
