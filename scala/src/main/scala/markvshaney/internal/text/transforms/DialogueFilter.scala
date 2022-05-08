package markvshaney.internal.text.transforms

import markvshaney.internal.domain.{TextTransform, TokenStream}

import scala.annotation.tailrec
import scala.collection.AbstractIterator

/** Transforms the token stream by removing dialogue */
object DialogueFilter extends TextTransform {

  val transform: TokenStream => TokenStream = (tokens: Iterator[String]) => {
    // Not lazy or efficient, but cheap to implement

    @tailrec
    def recursivelyRemoveDialogue(allTokens: List[String]): List[String] = {
      if (allTokens.exists(containsQuotes)) {
        val beforeDialogue =
          allTokens
            .takeWhile(!containsQuotes(_))

        val afterDialogue =
          allTokens
            .dropWhile(!containsQuotes(_))
            .drop(1)
            .dropWhile(!containsQuotes(_))
            .drop(1)

        recursivelyRemoveDialogue(beforeDialogue ++ afterDialogue)
      }
      else {
        allTokens
      }
    }

    val allTokens = List.from(tokens)
    recursivelyRemoveDialogue(allTokens).iterator
  }

  private def containsQuotes(s: String): Boolean = {
    s.contains("\"")
  }
}

