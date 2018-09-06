package response

import domain.Post
import play.api.libs.json._

final case class RedditResponse(status: String, posts: List[Post] = List.empty) {

}

object RedditResponse {

  implicit object RedditResponseFormatter extends Format[RedditResponse] {

    override def writes(redditResponse: RedditResponse): JsValue = {
      val productSeq = Seq(
        "Status" -> JsString(redditResponse.status),
        "Posts" -> Json.toJson(redditResponse.posts)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[RedditResponse] = ???
  }

}