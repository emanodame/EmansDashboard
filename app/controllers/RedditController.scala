package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.RedditService

class RedditController @Inject()(cc: ControllerComponents, redditService: RedditService) extends AbstractController(cc) {

  def getHHHPosts = Action {
    Ok(Json.prettyPrint(Json.toJson(redditService.getHHHPosts)))
  }

  def getCsCareerControllerPosts = Action {
    Ok(Json.prettyPrint(Json.toJson(redditService.getCsCareerPosts)))
  }

  def getTheRedPillPosts = Action {
    Ok(Json.prettyPrint(Json.toJson(redditService.getTheRedPillPosts)))
  }

  def getComboNews(quantity: Int) = Action {
    Ok(Json.prettyPrint(Json.toJson(redditService.getComboPosts(quantity))))
  }
}
