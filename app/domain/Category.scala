package domain

import play.api.libs.json._

object Category {

  sealed trait Category

  case object TopNews extends Category

  case object WorldNews extends Category

  case object UKNews extends Category

  case object TechNews extends Category

  case object FrontPage extends Category

  case object HHHPost extends Category

  case object TheRedPillPost extends Category

  case object CsCareerPost extends Category

  case object Unknown extends Category

  implicit object CategoryFormatter extends Format[Category] {

    override def writes(category: Category): JsValue = {
      val productSeq = Seq("category" -> JsString(category.toString))

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[Category] = ???
  }

}
