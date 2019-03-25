package controllers

import db.CarsRepository
import forms.CarForm
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import serializers.{CarWrites, FormErrorWrites}

@Singleton
class CarsController @Inject() (cc: ControllerComponents, repo: CarsRepository) extends AbstractController(cc)  {
  implicit val carWrites = CarWrites()
  implicit val formErrorWrites = FormErrorWrites()

  def index() = Action {
    Ok(Json.toJson(repo.findAll()))
  }

  def create() = Action(parse.form(CarForm.form(), onErrors = (formWithErrors: Form[CarForm]) => {
    BadRequest(Json.obj("errors" -> Json.toJson(formWithErrors.errors)))
  })) { req =>
    val carForm = req.body
    val car = carForm.toModel()

    Created(Json.toJson(repo.create(car)))
  }
}
