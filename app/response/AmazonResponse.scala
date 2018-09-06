package response

import domain.AmazonProduct
import play.api.libs.json._

final case class AmazonResponse(status: String, products: List[AmazonProduct] = List.empty) {
}

object AmazonResponse {

  implicit object AmazonResponseFormatter extends Format[AmazonResponse] {

    override def writes(amazonResponse: AmazonResponse): JsValue = {
      val productSeq = Seq(
        "Status" -> JsString(amazonResponse.status),
        "Products" -> Json.toJson(amazonResponse.products)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[AmazonResponse] = ???
  }

}
