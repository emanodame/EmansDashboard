package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service.NewsService

class NewsController @Inject()(cc: ControllerComponents, newsService: NewsService) extends AbstractController(cc) {

  def getTopNews = Action {
    Ok(Json.prettyPrint(Json.toJson(newsService.getTopNews)))
  }

  def getWorldsNews = Action {
    Ok(Json.prettyPrint(Json.toJson(newsService.getWorldNews)))
  }

  def getUkNews = Action {
    Ok(Json.prettyPrint(Json.toJson(newsService.getUKNews)))
  }

  def getTechNews = Action {
    Ok(Json.prettyPrint(Json.toJson(newsService.getTechNews)))
  }

  def getComboNews(quantity: Int) = Action {
    Ok(Json.prettyPrint(Json.toJson(newsService.getComboNews(quantity))))
  }
}