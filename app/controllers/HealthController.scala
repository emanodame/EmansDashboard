package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

class HealthController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getHealthCheck = Action {
    Ok("Healthy!")
  }
}
