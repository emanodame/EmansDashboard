package domain

import play.api.libs.json._

final case class AmazonProduct(name: String,
                               link: String,
                               price: BigDecimal,
                               rating: Double,
                               numberOfRatings: Int,
                               prime: Boolean) {
}

object AmazonProduct {

  implicit object AmazonProductFormatter extends Format[AmazonProduct] {

    override def writes(product: AmazonProduct): JsValue = {
      val productSeq = Seq(
        "name" -> JsString(product.name),
        "link" -> JsString(product.link),
        "price" -> JsNumber(product.price),
        "rating" -> JsNumber(product.rating),
        "numberOfRatings" -> JsNumber(product.numberOfRatings),
        "prime" -> JsBoolean(product.prime)
      )

      JsObject(productSeq)
    }

    override def reads(json: JsValue): JsResult[AmazonProduct] = ???
  }

}