package services

import domain.Category._
import domain.News
import response.NewsResponse
import util.CustomIO

import scala.xml.NodeSeq

class NewsService {
  private val topNewsUrl = "http://feeds.bbci.co.uk/news/rss.xml"
  private val worldNewsUrl = "http://feeds.bbci.co.uk/news/world/rss.xml"
  private val ukNewsUrl = "http://feeds.bbci.co.uk/news/uk/rss.xml"
  private val techNewsUrl = "http://feeds.bbci.co.uk/news/technology/rss.xml"

  def getTopNews: NewsResponse = generateResponse(topNewsUrl, TopNews)

  def getWorldNews: NewsResponse = generateResponse(worldNewsUrl, WorldNews)

  def getUKNews: NewsResponse = generateResponse(ukNewsUrl, UKNews)

  def getTechNews: NewsResponse = generateResponse(techNewsUrl, TechNews)

  def getComboNews(quantityOfNews: Integer): NewsResponse = {
    NewsResponse("Success",
      getTopNews.news.take(quantityOfNews) ++
        getWorldNews.news.take(quantityOfNews) ++
        getUKNews.news.take(quantityOfNews) ++
        getTechNews.news.take(quantityOfNews))
  }

  private def generateResponse(url: String, category: Category): NewsResponse = {
    generateNews(url, category) match {
      case Left(errorInfo) => NewsResponse(errorInfo)
      case Right(news) => NewsResponse("Success!", news)
    }
  }

  private def generateNews(url: String, category: Category) = {
    val newsIoMonad = CustomIO.getXmlFromWebsite(url)
    newsIoMonad.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity." + url),
        news => Right((news \ "channel" \ "item").map(item =>
          News(
            title = retrieveTextFromXml(item \ "title"),
            description = retrieveTextFromXml(item \ "description"),
            url = retrieveTextFromXml(item \ "link"),
            category)).toList))
  }

  private def retrieveTextFromXml(seq: NodeSeq): String = {
    seq.headOption match {
      case Some(xml) => xml.text
      case _ => "Unknown"
    }
  }
}
