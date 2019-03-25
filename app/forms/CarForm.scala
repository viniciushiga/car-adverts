package forms

import java.time.LocalDate
import java.util.UUID

import models.Car
import models.Fuel.Fuel
import play.api.data.Form
import play.api.data.Forms._
import serializers.FuelFormatter

case class CarForm(title: String, fuel: Fuel, price: Int, `new`: Boolean, mileage: Option[Int], firstRegistration: Option[LocalDate])

object CarForm {
  implicit val fuelFormatter = FuelFormatter()

  def form() = {
    Form(
      mapping(
        "title" -> text,
        "fuel" -> of[Fuel],
        "price" -> number,
        "new" -> boolean,
        "mileage" -> optional(number),
        "firstRegistration" -> optional(localDate)
      )(CarForm.apply)(CarForm.unapply) verifying("used.car", carForm =>
        (carForm.`new`, carForm.mileage, carForm.firstRegistration) match {
          case (true, None, None) => true
          case (false, Some(_), Some(_)) => true
          case _ => false
        })
    )
  }

  implicit class ConversionOps(self: CarForm) {
    def toModel(): Car = {
      Car(
        UUID.randomUUID(),
        self.title,
        self.fuel,
        self.price,
        self.`new`,
        self.mileage,
        self.firstRegistration
      )
    }
  }
}
