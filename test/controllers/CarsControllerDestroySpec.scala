package controllers

import java.util.UUID

import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class CarsControllerDestroySpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DELETE /cars/:id" should {
    "deletes car when it exits" in {
      val id = UUID.randomUUID().toString
      val request = FakeRequest(DELETE, s"/cars/$id")
      val show = route(app, request).get

      status(show) mustBe NO_CONTENT
    }

    "returns 404 car when does not exit" in {
      val id = "00000000-0000-0000-0000-000000000000"
      val request = FakeRequest(DELETE, s"/cars/$id")
      val show = route(app, request).get

      status(show) mustBe NOT_FOUND
    }
  }
}
