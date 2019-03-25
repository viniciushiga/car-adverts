package serializers

import models.Car
import play.api.libs.json.{JsValue, Json, Writes}

class CarWrites extends Writes[Car] {
  implicit val fuelWrites = FuelWrites()

  override def writes(car: Car): JsValue = Json.obj(
    "id" -> car.id,
    "title" -> car.title,
    "fuel" -> Json.toJson(car.fuel),
    "price" -> car.price,
    "new" -> car.`new`,
    "mileage" -> car.mileage,
    "firstRegistration" -> car.firstRegistration
  )
}

object CarWrites {
  def apply(): CarWrites = new CarWrites()
}
