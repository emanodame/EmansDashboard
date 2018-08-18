package domain

import play.api.libs.json._

case class Media(name: String,
                 year: String,
                 length: String,
                 imdbScore: Float,
                 metaScore: Int) {
}

object Media {

  implicit object MediaFormatter extends Format[Media] {

    override def writes(media: Media): JsValue = {
      val productSeq = Seq(
        "title" -> JsString(media.name),
        "IMDB" -> JsNumber(media.imdbScore.toDouble),
        "Metacritic" -> JsNumber(media.metaScore),
        "length" -> JsString(media.length),
        "year" -> JsString(media.year)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[Media] = ???
  }

}
