package markvshaney.internal.text.transforms

import markvshaney.internal.domain.{TextTransform, TokenStream}

/** Transforms the token stream by trimming it to be at least as long as the desired length and is formed of complete sentences */
class LengthTrimmer(minLength: Int) extends TextTransform {

  val transform: TokenStream => TokenStream = (tokens: Iterator[String]) => {
    val trimmedToStart = tokens.dropWhile(!isStart(_))
    takeUntilEnd(minLength, trimmedToStart)
  }

  private def isStart(s: String): Boolean = {
    s(0).toUpper == s(0)
  }

  private def isEnd(s: String): Boolean = {
    s.matches(".+[.?!]$")
  }

  private def takeUntilEnd(n: Int, text: TokenStream): TokenStream = {
    text
      .zipWithIndex
      .takeUntil { case (s, idx) => idx >= minLength && isEnd(s) }
      .map(_.head)
  }

  implicit class TakeUntilIteratorWrapper[T](items: Iterator[T]) {
    def takeUntil(predicate: T => Boolean): Iterator[T] = {
      items.span(!predicate(_)) match {
        case (head, tail) => head ++ tail.take(1)
      }
    }
  }
}
