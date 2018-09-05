package response

import domain.{EmptySongCreator, Song}
import play.api.libs.json._

final case class SongResponse(status: String, song: Song = EmptySongCreator.create) {
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
