package domain

import domain.Category.Category
import play.api.libs.json._

final case class Post(title: String, url: String, category: Category) {

  override def toString: String = {
    title.concat(" " + url)
  }
}

object Post {

  implicit object PostFormatter extends Format[Post] {

    override def writes(post: Post): JsValue = {
      val productSeq = Seq(
        "title" -> JsString(post.title),
        "url" -> JsString(post.url),
        "category" -> JsString(post.category.toString)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[Post] = ???
  }

}
