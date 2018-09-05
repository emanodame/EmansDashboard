package services

import domain.Song
import util.CustomIO
import play.api.libs.json.{Json, _}
import response.SongResponse

final class SongService {
  private val apiKey = "V4BQYEA_PgtWJW3SV7_G9qiV2yOA4WbwKGBVoPl5ROCL4SFWFvGzYQccLtpU6Hro"
  private val apiSearchLink = "https://api.genius.com/search?q="

  def getSongInfo(songName: String): SongResponse = {
    val songId = retrieveSongId(songName)

    songId match {
      case Left(errorInfo) => SongResponse(errorInfo)
      case Right(retrievedSongId) => retrieveSong(retrievedSongId) match {
        case Left(errorInfo) => SongResponse(errorInfo)
        case Right(song) => SongResponse("Success! ", createSongObject(song))
      }
    }
  }

  private def retrieveSongId(songName: String): Either[String, JsValue] = {
    val searchLink = generateSearchLink(songName)

    val geniusStringResponse = CustomIO.getHtmlFromWebsiteViaHttp(searchLink, apiKey)
    geniusStringResponse.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity"),
        retrievedJson => parseJsonForSongId(retrievedJson))
  }

  private def parseJsonForSongId(retrievedJson: String): Either[String, JsValue] = {
    (Json.parse(retrievedJson) \ "response" \ "hits").as[JsArray].value.headOption match {
      case Some(json) => Right((json \ "result" \ "id").get)
      case _ => Left("No Song found!")
    }
  }

  private def generateSearchLink(songName: String): String = {
    val formattedSongName = songName.replace(" ", "%20")
    apiSearchLink.concat(formattedSongName)
  }

  private def retrieveSong(songId: JsValue): Either[String, JsValue] = {
    val searchLink = "https://api.genius.com/songs/" + songId

    val geniusStringResponse = CustomIO.getHtmlFromWebsiteViaHttp(searchLink, apiKey)
    geniusStringResponse.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity"),
        retrievedJson => Right((Json.parse(retrievedJson) \ "response" \ "song").get))
  }

  private def createNameToUrlConnection(result: JsLookupResult): List[Map[String, String]] = {
    result.as[JsArray].value.map(entry =>
      Map[String, String]((entry \ "name").get.toString() -> (entry \ "url").get.toString())).toList
  }

  private def createSongObject(song: JsValue): Song = {
    Song(
      id = (song \ "id").get.toString(),
      name = (song \ "full_title").get.toString(),
      url = (song \ "url").get.toString(),
      features = createNameToUrlConnection(song \ "featured_artists"),
      producers = createNameToUrlConnection(song \ "producer_artists"))
  }
}