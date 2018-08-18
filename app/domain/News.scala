package domain

import domain.Category.Category
import play.api.libs.json._

final case class News(title: String, description: String, url: String, category: Category) {

  override def toString: String = {
    title.concat(" " + description)
      .concat(" " + url)
  }
}

object News {

  implicit object NewsFormatter extends Format[News] {

    override def writes(news: News): JsValue = {
      val productSeq = Seq(
        "title" -> JsString(news.title),
        "description" -> JsString(news.description),
        "url" -> JsString(news.url),
        "category" -> JsString(news.category.toString)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[News] = ???
  }

}
