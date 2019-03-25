package serializers

import play.api.data.FormError
import play.api.libs.json.{JsValue, Json, Writes}

class FormErrorWrites extends Writes[FormError] {
  override def writes(o: FormError): JsValue = Json.obj(
    "key" -> Json.toJson(o.key),
    "message" -> Json.toJson(o.message),
  )
}

object FormErrorWrites {
  def apply(): FormErrorWrites = new FormErrorWrites()
}
