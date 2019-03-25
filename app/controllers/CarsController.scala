package controllers

import db.CarsRepository
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import serializers.CarWrites

@Singleton
class CarsController @Inject() (cc: ControllerComponents, repo: CarsRepository) extends AbstractController(cc)  {
  implicit val carWrites = CarWrites()

  def index() = Action {
    Ok(Json.toJson(repo.findAll()))
  }
}
