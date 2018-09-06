package services

import domain.Media
import util.CustomIO
import play.api.libs.json._
import response.MediaResponse

class MediaService {
  private val searchLink = "http://www.omdbapi.com/?apikey=1c6af724&t="
  private val minImdbScore = 7
  private val minMetacriticScore = 65

  def determineIfWatchable(mediaName: String): MediaResponse = {
    val mediaObject = getMediaObject(mediaName)

    mediaObject match {
      case Left(errorInfo) => MediaResponse(errorInfo)
      case Right(media) =>
        val determineIfWatchable = (media.imdbScore >= minImdbScore) && (media.metaScore >= minMetacriticScore)
        MediaResponse("Success", Map(determineIfWatchable -> media))
    }
  }

  private def getMediaObject(mediaName: String): Either[String, Media] = {
    val formattedMediaName = mediaName.replace(' ', '+')
    val mediaIoMonad = CustomIO.getHtmlFromWebsiteViaHttp(searchLink + formattedMediaName)

    mediaIoMonad.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity"),
        retrievedMediaHtml => Right(createMediaObject(retrievedMediaHtml)))
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
      case Some(i) =>
        val score = i.toString().replace("\"", "")
        if (score.forall(_.isDigit)) score.toInt else 0
      case None => 0
    }
  }
}
