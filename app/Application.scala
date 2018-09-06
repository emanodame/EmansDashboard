import services._

object Application {
  def main(args: Array[String]): Unit = {
    val redditService = new RedditService
    val newsService = new NewsService
    val amazonService = new AmazonService
    val movieService = new MediaService
    val songService  = new SongService

//    songService.getSongInfo("Kitchen cudi")
    movieService.determineIfWatchable("Game of Thrones")
  }
}
