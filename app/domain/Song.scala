package domain

import play.api.libs.json._

final case class Song(id: String,
                      name: String,
                      url: String,
                      features: List[Map[String, String]],
                      producers: List[Map[String, String]]) {
}

object EmptySongCreator {

  def create: Song = {
    Song(
      id = "N/A",
      name = "N/A",
      url = "N/A",
      features = List(),
      producers = List())
  }
}

object Song {

  implicit object SongFormatter extends Format[Song] {

    override def writes(song: Song): JsValue = {
      val productSeq = Seq(
        "id" -> JsString(song.id),
        "name" -> JsString(song.name),
        "url" -> JsString(song.url),
        "features" -> Json.toJson(song.features),
        "producers" -> Json.toJson(song.producers)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[Song] = ???
  }

}