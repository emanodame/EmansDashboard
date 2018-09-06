package response

import domain.News
import play.api.libs.json._

final case class NewsResponse(status: String, news: List[News] = List.empty) {
}

object NewsResponse {

  implicit object NewsResponseFormatter extends Format[NewsResponse] {

    override def writes(newsResponse: NewsResponse): JsValue = {
      val productSeq = Seq(
        "Status" -> JsString(newsResponse.status),
        "News" -> Json.toJson(newsResponse.news)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[NewsResponse] = ???
  }

}
