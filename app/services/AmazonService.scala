package services

import domain.AmazonProduct
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.model.ElementQuery
import response.AmazonResponse
import util.CustomIO

class AmazonService {
  private val amountOfProductsToRetrieve = 500
  private val searchLink = "https://www.amazon.co.uk/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords="
  private val productTitleHtmlClass = "[title]"
  private val productLinkHtmlClass = ".a-text-normal"
  private val productPriceHtmlClass = ".s-price"
  private val productRatingHtmlClass = ".a-icon-star"
  private val numberOfRatingsHtmlClass = "[href*=customerReviews]"
  private val primeHtmlClass = ".a-icon-prime"

  def getSuitableProducts(productName: String, maxPrice: Double = Double.MaxValue): AmazonResponse = {
    val productsFromAmazon = getProductsFromAmazon(productName)

    productsFromAmazon match {
      case Left(errorInfo) => AmazonResponse(errorInfo)
      case Right(products) => AmazonResponse("Success: ", sortAndFilterProducts(products, maxPrice))
    }
  }

  private def sortAndFilterProducts(products: List[AmazonProduct], maxPrice: Double): List[AmazonProduct] = {
    val filteredProducts = applyPricePrimeFilteringOnProducts(products, maxPrice)
    sortProductsByReviews(filteredProducts)
  }

  private def getProductsFromAmazon(productName: String): Either[String, List[AmazonProduct]] = {
    val productSearchLink = searchLink + productName
    val amazonIoMonad = CustomIO.getHtmlFromWebsiteViaJsoup(productSearchLink)

    amazonIoMonad.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity"),
        retrievedHtmlProducts => Right(

          (0 to amountOfProductsToRetrieve)
            .filter(index => assertValidProduct(retrievedHtmlProducts.select("#result_" + index)))
            .map(item => {

              val product = retrievedHtmlProducts.select("#result_" + item)

              createProductObject(
                productName = product.select(productTitleHtmlClass).head.attr("title"),
                link = product.select(productLinkHtmlClass).head.attr("href"),
                price = product.select(productPriceHtmlClass).head.text,
                rating = product.select(productRatingHtmlClass).head.text,
                numberOfRatings = product.select(numberOfRatingsHtmlClass).head.text,
                prime = product.select(primeHtmlClass).head.select("[aria-label]").nonEmpty)

            }).toList))
  }

  private def assertValidProduct(selectedProduct: ElementQuery[Browser#DocumentType#ElementType]): Boolean = {
    selectedProduct.select(productTitleHtmlClass).nonEmpty &&
      selectedProduct.select(productLinkHtmlClass).nonEmpty &&
      selectedProduct.select(productPriceHtmlClass).nonEmpty &&
      selectedProduct.select(productRatingHtmlClass).nonEmpty &&
      selectedProduct.select(numberOfRatingsHtmlClass).nonEmpty &&
      selectedProduct.select(primeHtmlClass).nonEmpty
  }

  private def createProductObject(productName: String,
                                  link: String,
                                  price: String,
                                  rating: String,
                                  numberOfRatings: String,
                                  prime: Boolean): AmazonProduct = {

    AmazonProduct(
      productName,
      link = link,
      price = BigDecimal(price.split(" ").head.replaceAll("[£,]", "")), //remove pound sign and comma from string
      rating = rating.split(" ").head.toDouble, //remove trailing 'out of 5' string
      numberOfRatings = numberOfRatings.replace(",", "").toInt, // remove comma from string
      prime = prime)
  }

  private def applyPricePrimeFilteringOnProducts(amazonProducts: List[AmazonProduct], maxPrice: Double): List[AmazonProduct] = {
    amazonProducts.filter(_.price <= maxPrice).filter(_.prime)
  }

  private def sortProductsByReviews(products: List[AmazonProduct]): List[AmazonProduct] = {
    val totalNumberOfReviews = products.foldLeft(0)(_ + _.numberOfRatings)
    products.sortBy(l => (l.numberOfRatings * l.rating) / totalNumberOfReviews).reverse
  }
}
