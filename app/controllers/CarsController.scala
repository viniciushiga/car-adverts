package controllers

import java.util.UUID

import akka.actor.ActorSystem
import db.CarsRepository
import forms.CarForm
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import serializers.{CarWrites, FormErrorWrites}

@Singleton
class CarsController @Inject() (cc: ControllerComponents, repo: CarsRepository, system: ActorSystem) extends AbstractController(cc)  {
  implicit val carWrites = CarWrites()
  implicit val formErrorWrites = FormErrorWrites()
  implicit val context = system.dispatcher

  def index() = Action.async {
    repo.findAll().map(cars => Ok(Json.toJson(cars)))
  }

  def create() = Action.async(parse.form(CarForm.form(), onErrors = (formWithErrors: Form[CarForm]) => {
    BadRequest(Json.obj("errors" -> Json.toJson(formWithErrors.errors)))
  })) { req =>
    val carForm = req.body

    repo.create(carForm.toModel()).map {
      case Some(c) => Created(Json.toJson(c))
        .withHeaders("Location" -> s"/cars/${c.id}")
      case None => InternalServerError
    }
  }

  def show(id: UUID) = Action.async {
    repo.find(id).map {
      case Some(car) => Ok(Json.toJson(car))
      case None => NotFound
    }
  }

  def update(id: UUID) = Action.async(parse.form(CarForm.form(), onErrors = (formWithErrors: Form[CarForm]) => {
    BadRequest(Json.obj("errors" -> Json.toJson(formWithErrors.errors)))
  })) { req =>
    val carForm = req.body
    repo.update(carForm.toModel(id)).map {
      case Some(c) => Ok(Json.toJson(c))
      case None => NotFound
    }
  }

  def destroy(id: UUID) = Action.async {
    repo.delete(id).map {
      case Some(_) => NoContent
      case None => NotFound
    }
  }
}
