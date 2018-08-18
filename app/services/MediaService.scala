package services

import domain.Media
import play.api.libs.json._
import scalaj.http.Http

class MediaService {
  private val searchLink = "http://www.omdbapi.com/?apikey=1c6af724&t="
  private val minImdbScore = 7
  private val minMetacriticScore = 65

  def determineIfWatchable(mediaName: String): Map[Boolean, Media] = {
    val mediaObject = getMediaObject(mediaName)
    val determineIfWatchable = (mediaObject.imdbScore >= minImdbScore) && (mediaObject.metaScore >= minMetacriticScore)
    Map(determineIfWatchable -> mediaObject)
  }

  private def getMediaObject(mediaName: String): Media = {
    val formattedMediaName = mediaName.replace(' ', '+')
    val retrievedStringJson = Http(searchLink + formattedMediaName).asString.body
    createMediaObject(retrievedStringJson)
  }

  private def createMediaObject(retrievedJsonString: String): Media = {
    val jsonObject = Json.parse(retrievedJsonString)

    Media(
      name = (jsonObject \ "Title").get.toString(),
      year = (jsonObject \ "Year").get.toString(),
      length = (jsonObject \ "Runtime").get.toString(),
      imdbScore = retrieveImdbScore((jsonObject \ "imdbRating").toOption),
      metaScore = retrieveMetaScore((jsonObject \ "Metascore").toOption))
  }

  private def retrieveImdbScore(imdbScore: Option[JsValue]): Float = {
    imdbScore match {
      case Some(i) => i.toString.replace("\"", "").toFloat
      case None => 0
    }
  }

  private def retrieveMetaScore(metaScore: Option[JsValue]): Int = {
    metaScore match {
      case Some(i) => i.toString.replace("\"", "").toInt
      case None => 0
    }
  }
}
