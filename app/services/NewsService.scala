package services

import domain.Category._
import domain.News

import scala.xml.XML

class NewsService {
  private val topNewsUrl = "http://feeds.bbci.co.uk/news/rss.xml"
  private val worldNewsUrl = "http://feeds.bbci.co.uk/news/world/rss.xml"
  private val ukNewsUrl = "http://feeds.bbci.co.uk/news/uk/rss.xml"
  private val techNewsUrl = "http://feeds.bbci.co.uk/news/technology/rss.xml"

  def getTopNews: Seq[News] = generateItems(topNewsUrl, TopNews)

  def getWorldNews: Seq[News] = generateItems(worldNewsUrl, WorldNews)

  def getUKNews: Seq[News] = generateItems(ukNewsUrl, UKNews)

  def getTechNews: Seq[News] = generateItems(techNewsUrl, TechNews)

  def getComboNews(quantityOfNews: Integer): Seq[News] = {
    getTopNews.take(quantityOfNews) ++
      getWorldNews.take(quantityOfNews) ++
      getUKNews.take(quantityOfNews) ++
      getTechNews.take(quantityOfNews)
  }

  private def generateItems(url: String, category: Category): Seq[News] = {
    val xmlItems = XML.load(url) \ "channel" \ "item"
    xmlItems.map(item => News(
      (item \ "title").head.text,
      (item \ "description").head.text,
      (item \ "link").head.text,
      category))
  }
}
