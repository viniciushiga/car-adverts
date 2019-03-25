package db

import java.time.LocalDate
import java.util.UUID

import javax.inject.Singleton
import models.{Car, Fuel}

@Singleton
class CarsRepository {
  def findAll(): Seq[Car] = {
    Seq(
      Car(UUID.randomUUID(), "Audi A3", Fuel.Gasoline, 1500000, true, None, None),
      Car(UUID.randomUUID(), "BMW 120d", Fuel.Diesel, 1700000, false, Some(20000), Some(LocalDate.of(2018, 12, 25))),
    )
  }

  def create(car: Car): Car = {
    car
  }

  def update(car: Car): Car = {
    car
  }
}
