package response

import domain.Song
import play.api.libs.json._

final case class SongResponse(status: String, song: Song) {

  def this(response: String) = {
    this(response, Song("Invalid", "Invalid", "Invalid", List(), List()))
  }
}

object SongResponse {

  implicit object SongResponseFormatter extends Format[SongResponse] {

    override def writes(songResponse: SongResponse): JsValue = {
      val productSeq = Seq(
        "Status" -> JsString(songResponse.status),
        "Song" -> Json.toJson(songResponse.song)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[SongResponse] = ???
  }

}
