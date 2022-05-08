package markvshaney.internal.text

import markvshaney.internal.text.WhitespaceTokenizer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WhitespaceTokeniserTest extends AnyFlatSpec with Matchers {

  behavior of "tokenize"

  it should "split lines into tokens on whitespace" in {
    // Given
    val inputText =
      """a  b
        |c
        |  d""".stripMargin

    // When
    val tokens = WhitespaceTokenizer.tokenize(lines(inputText))

    // Then
    tokens.toList shouldBe List("a", "b", "c", "d")
  }

  private def lines(s: String): Iterator[String] =
    s.split(" ").iterator
}
