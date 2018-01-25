import scala.io._
import scala.actors._
import Actor._

// START:loader
object PageLoader {
    def getPageSize(url : String) = Source.fromURL(url).mkString.length
    def getNumLinks(url: String) =
    {
        val page = Source.fromURL(url).mkString
        "(?i)<a[^>]+?href".r.findAllIn(page).size
    }
}
// END:loader

object Main extends App
{
    val urls = List("http://www.amazon.com/",
                   "http://www.twitter.com/",
                   "http://www.google.com/",
                   "http://www.cnn.com/" )

    // START:time
    def timeMethod(method: () => Unit) = {
     val start = System.nanoTime
     method()
     val end = System.nanoTime
     println("Method took " + (end - start)/1000000000.0 + " seconds.")
    }
    // END:time

    // START:sequential
    def getPageSizeSequentially() = {
     for(url <- urls) {
        println("Size for " + url + ": " + PageLoader.getPageSize(url))
        println(("Number of links on " + url + ": " + PageLoader.getNumLinks(url)))
     }
    }
    // END:sequential

    // START:concurrent
    def getPageSizeConcurrently() = {
     val caller = self

     for(url <- urls) {
        actor { caller ! (0, url, PageLoader.getPageSize(url)) }
        actor { caller ! (1, url, PageLoader.getNumLinks(url)) }
     }

     for(i <- 1 to urls.size * 2) {
       receive {
         case (0, url, size) =>
           println("Size for " + url + ": " + size)
         case (1, url, links) =>
           println("Number of links on " + url + ": " + links)
       }
     }
    }
    // END:concurrent

    // START:script
    println("Sequential run:")
    timeMethod { getPageSizeSequentially }

    println("Concurrent run")
    timeMethod { getPageSizeConcurrently }
    // END:script
}
