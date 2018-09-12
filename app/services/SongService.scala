package services

import domain.Song
import javax.inject.Inject
import play.api.Configuration
import play.api.cache.SyncCacheApi
import util.CustomIO
import play.api.libs.json.{Json, _}
import response.SongResponse

final class SongService @Inject()(cache: SyncCacheApi, config: Configuration) {
  private val apiKey = config.get[String]("songService.apiKey")

  def getSongInfo(songName: String): SongResponse = {
    cache.get[Song](songName) match {

      case Some(song) => SongResponse("Successful Cache!", song)
      case None =>
        retrieveSongId(songName) match {

          case Left(errorInfo) => SongResponse(errorInfo)
          case Right(retrievedSongId) => retrieveSong(retrievedSongId) match {

            case Left(errorInfo) => SongResponse(errorInfo)
            case Right(song) =>
              cache.set(songName, createSongObject(song))
              SongResponse("Success! ", createSongObject(song))
          }
        }
    }
  }

  private def retrieveSongId(songName: String): Either[String, JsValue] = {
    val searchLink = generateSearchLink(songName)

    val geniusIoMonad = CustomIO.getHtmlFromWebsiteViaHttp(searchLink, apiKey)
    geniusIoMonad.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity"),
        geniusJsonResponse => parseJsonForSongId(geniusJsonResponse))
  }

  private def parseJsonForSongId(retrievedJson: String): Either[String, JsValue] = {
    (Json.parse(retrievedJson) \ "response" \ "hits").as[JsArray].value.headOption match {
      case Some(json) => Right((json \ "result" \ "id").get)
      case _ => Left("No Song found!")
    }
  }

  private def generateSearchLink(songName: String): String = {
    val formattedSongName = songName.replace(" ", "%20")
    "https://api.genius.com/search?q=".concat(formattedSongName)
  }

  private def retrieveSong(songId: JsValue): Either[String, JsValue] = {
    val searchLink = "https://api.genius.com/songs/" + songId

    val geniusIoMonad = CustomIO.getHtmlFromWebsiteViaHttp(searchLink, apiKey)
    geniusIoMonad.attempt
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