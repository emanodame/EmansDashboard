package controllers

import javax.inject.Inject
import play.api.Configuration
import play.api.mvc.{AbstractController, ControllerComponents}

class HealthController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {

  def getHealthCheck = Action {
    Ok("Version: " + config.get[String]("version"))
  }
}
