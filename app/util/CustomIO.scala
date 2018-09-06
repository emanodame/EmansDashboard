package util

import cats.effect.IO
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import scalaj.http.Http

import scala.xml.{Elem, XML}

object CustomIO {

  def getHtmlFromWebsiteViaHttp(link: String, apiKey: String = ""): IO[String] = {
    IO(Http(link)
      .param("access_token", apiKey)
      .asString
      .body)
  }

  def getHtmlFromWebsiteViaJsoup(link: String): IO[Browser#DocumentType#ElementType] = IO(JsoupBrowser().get(link).body)

  def getXmlFromWebsite(link: String): IO[Elem] = IO(XML.load(link))

}
