package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.MediaService

class MediaController @Inject()(cc: ControllerComponents, mediaService: MediaService) extends AbstractController(cc) {

  def getMedia(mediaName: String) = Action {
    Ok(Json.prettyPrint(Json.toJson(mediaService.determineIfWatchable(mediaName))))
  }
}
