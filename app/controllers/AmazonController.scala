package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service.AmazonService

class AmazonController @Inject()(cc: ControllerComponents, amazonService: AmazonService) extends AbstractController(cc) {

  def getSuitableProducts(name: String) = Action {
    Ok(Json.prettyPrint(Json.toJson(amazonService.getSuitableProducts(name))))
  }
}
