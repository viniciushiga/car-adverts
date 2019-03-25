package db

import java.time.LocalDate
import java.util.UUID

import com.gu.scanamo.DynamoFormat
import models.Fuel
import models.Fuel.Fuel

object DynamoFormats {
  implicit def uuidStringFormat: DynamoFormat[UUID] = DynamoFormat.coercedXmap[UUID, String, IllegalArgumentException] {
    str => UUID.fromString(str)
  } {
    uuid => uuid.toString
  }

  implicit def localDateFormat: DynamoFormat[LocalDate] = DynamoFormat.coercedXmap[LocalDate, String, IllegalArgumentException] {
    str => LocalDate.parse(str)
  } {
    date => date.toString
  }

  implicit def fuelFormat: DynamoFormat[Fuel] = DynamoFormat.coercedXmap[Fuel, String, IllegalArgumentException] {
    str => Fuel.withName(str)
  } {
    fuel => fuel.toString
  }
}
