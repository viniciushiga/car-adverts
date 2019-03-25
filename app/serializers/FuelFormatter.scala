package serializers

import models.Fuel
import models.Fuel.Fuel
import play.api.data.FormError
import play.api.data.format.Formats.parsing
import play.api.data.format.Formatter

class FuelFormatter extends Formatter[Fuel] {
  override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Fuel] = {
    parsing(str => Fuel.withName(str), "error.fuel", Nil)(key, data)
  }

  override def unbind(key: String, value: Fuel): Map[String, String] = Map(key -> value.toString)
}

object FuelFormatter {
  def apply(): FuelFormatter = new FuelFormatter()
}
