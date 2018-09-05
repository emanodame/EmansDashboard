package util

import cats.effect.IO
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import scalaj.http.Http

object CustomIO {

  def getHtmlFromWebsiteViaHttp(link: String, apiKey: String = ""): IO[String] = {
    IO(Http(link)
      .param("access_token", apiKey)
      .asString
      .body)
  }

  def getHtmlFromWebsiteViaJsoup(link: String): Browser#DocumentType#ElementType = JsoupBrowser().get(link).body

}
