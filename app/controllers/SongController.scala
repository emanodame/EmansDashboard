package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.SongService

class SongController @Inject()(cc: ControllerComponents, songService: SongService) extends AbstractController(cc) {

  def getSongInfo(songName: String) = Action {
    Ok(Json.prettyPrint(Json.toJson(songService.getSongInfo(songName))))
  }
}
