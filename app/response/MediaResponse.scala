package response

import domain.Media
import play.api.libs.json._

final case class MediaResponse(status: String, media: Map[Boolean, Media] = Map.empty) {

}

object MediaResponse {

  implicit object MediaResponseFormatter extends Format[MediaResponse] {

    override def writes(mediaResponse: MediaResponse): JsValue = {
      val productSeq = Seq(
        "Status" -> JsString(mediaResponse.status),
        "Media" -> Json.toJson(mediaResponse.media)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[MediaResponse] = ???
  }

}
