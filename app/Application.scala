import service.{AmazonService, NewsService, RedditService}
import services.MediaService

object Application {
  def main(args: Array[String]): Unit = {
    val redditService = new RedditService
    val newsService = new NewsService
    val amazonService = new AmazonService
    val movieService = new MediaService
  }
}
