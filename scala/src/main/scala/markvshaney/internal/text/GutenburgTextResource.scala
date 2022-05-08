package markvshaney.internal.text

import markvshaney.MarkVShaney.getClass
import markvshaney.internal.domain.TokenStream

import scala.io.Source

object GutenburgTextResource {

  def readLines(resourceName: String): Iterator[String] = {
    val inputStream = getClass.getResourceAsStream(resourceName)
    val lines = Source.fromInputStream(inputStream).getLines

    lines
      .dropWhile(!isStartOfEBook(_))
      .drop(1)
      .takeWhile(!isEndOfEBook(_))
  }

  private def isStartOfEBook(s: String): Boolean = {
    s.toLowerCase.matches(".*start of (the|this) project gutenberg ebook.*")
  }

  private def isEndOfEBook(s: String): Boolean = {
    s.toLowerCase.matches(".*end of (the|this) project gutenberg ebook.*")
  }
}
