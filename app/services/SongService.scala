package services

import domain.{EmptySongCreator, Song}
import play.api.libs.json._
import response.SongResponse
import scalaj.http.Http

class SongService {
  private val apiKey = "V4BQYEA_PgtWJW3SV7_G9qiV2yOA4WbwKGBVoPl5ROCL4SFWFvGzYQccLtpU6Hro"

  def getSongInfo(songName: String): SongResponse = {
    val songId = retrieveSongId(songName)

    songId match {
      case Some(id) => SongResponse("Success", createSongObject(id))
      case None => SongResponse("No Result for song name: " + songName, EmptySongCreator.create)
    }
  }

  private def retrieveSongId(songName: String): Option[JsValue] = {
    val formattedSongName = songName.replace(" ", "%20")
    val geniusStringResponse = Http("https://api.genius.com/search?q=" + formattedSongName)
      .param("access_token", apiKey)
      .asString
      .body

    val jsonObject = (Json.parse(geniusStringResponse) \ "response" \ "hits").as[JsArray]

    jsonObject.value.toList match {
      case Nil => None
      case _ => (jsonObject.head \ "result" \ "id").toOption
    }
  }

  private def createSongObject(songId: JsValue): Song = {
    val geniusStringResponse = Http("https://api.genius.com/songs/" + songId)
      .param("access_token", apiKey)
      .asString
      .body

    val jsonSongObject = Json.parse(geniusStringResponse) \ "response" \ "song"

    Song(
      id = songId.toString(),
      name = (jsonSongObject \ "full_title").get.toString(),
      url = (jsonSongObject \ "url").get.toString(),
      features = createList(jsonSongObject \ "featured_artists"),
      producers = createList(jsonSongObject \ "producer_artists"))
  }

  private def createList(result: JsLookupResult): List[Map[String, String]] = {
    result.as[JsArray].value.map(entry =>
      Map[String, String]((entry \ "name").get.toString() -> (entry \ "url").get.toString())).toList
  }
}
