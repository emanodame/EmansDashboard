package service

import domain.Category._
import domain.Post
import scalaj.http.Http

import scala.xml.XML

class RedditService {
  private val frontPageUrl = "https://www.reddit.com/.rss"
  private val hhhUrl = "https://www.reddit.com/r/hiphopheads/.rss"
  private val csCareerUrl = "https://www.reddit.com/r/hiphopheads/.rss"
  private val theRedPillUrl = "https://www.reddit.com/r/TheRedPill/.rss"

  def getFrontPagePosts: Seq[Post] = generatePosts(frontPageUrl, Unknown)

  def getHHHPosts: Seq[Post] = generatePosts(hhhUrl, HHHPost)

  def getCsCareerPosts: Seq[Post] = generatePosts(csCareerUrl, CsCareerPost)

  def getTheRedPillPosts: Seq[Post] = generatePosts(theRedPillUrl, TheRedPillPost)

  def getComboNews(quantityOfNews: Integer): Seq[Post] = {
    getHHHPosts.take(quantityOfNews) ++
      getCsCareerPosts.take(quantityOfNews) ++
      getTheRedPillPosts.take(quantityOfNews)
  }

  private def generatePosts(url: String, category: Category): Seq[Post] = {
    val hhhContent = Http(url).asString.body
    (XML.loadString(hhhContent) \ "entry")
      .map(post => Post(
        (post \ "title").head.text,
        (post \\ "link" \\ "@href").head.text,
        category))
  }
}
