package serializers

import models.Fuel.Fuel
import play.api.libs.json.{JsString, JsValue, Writes}

class FuelWrites extends Writes[Fuel] {
  override def writes(fuel: Fuel): JsValue = JsString(fuel.toString)
}

object FuelWrites {
  def apply(): FuelWrites = new FuelWrites()
}
