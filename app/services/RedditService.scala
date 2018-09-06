package services

import domain.Category._
import domain.Post
import response.RedditResponse
import util.CustomIO

import scala.xml.XML

class RedditService {
  private val frontPageUrl = "https://www.reddit.com/.rss"
  private val hhhUrl = "https://www.reddit.com/r/hiphopheads/.rss"
  private val csCareerUrl = "https://www.reddit.com/r/cscareerquestions/.rss"
  private val theRedPillUrl = "https://www.reddit.com/r/TheRedPill/.rss"

  def getFrontPagePosts: RedditResponse = generateResponse(frontPageUrl, Unknown)

  def getHHHPosts: RedditResponse = generateResponse(hhhUrl, HHHPost)

  def getCsCareerPosts: RedditResponse = generateResponse(csCareerUrl, CsCareerPost)

  def getTheRedPillPosts: RedditResponse = generateResponse(theRedPillUrl, TheRedPillPost)

  def getComboPosts(quantityOfPosts: Int): RedditResponse = {
    RedditResponse("Success",
      getHHHPosts.posts.take(quantityOfPosts) ++
        getCsCareerPosts.posts.take(quantityOfPosts) ++
        getTheRedPillPosts.posts.take(quantityOfPosts))
  }

  private def generateResponse(url: String, category: Category): RedditResponse = {
    generatePosts(url, category) match {
      case Left(errorInfo) => RedditResponse(errorInfo)
      case Right(posts) => RedditResponse("Success!", posts)
    }
  }

  private def generatePosts(url: String, category: Category): Either[String, List[Post]] = {
    val redditIoMonad = CustomIO.getHtmlFromWebsiteViaHttp(url)
    redditIoMonad.attempt
      .unsafeRunSync()
      .fold(_ => Left("Failure; Check network connectivity"),
        redditContent => Right((XML.loadString(redditContent) \ "entry")
          .map(post => Post(
            (post \ "title").head.text,
            (post \\ "link" \\ "@href").head.text,
            category)).toList))
  }
}
