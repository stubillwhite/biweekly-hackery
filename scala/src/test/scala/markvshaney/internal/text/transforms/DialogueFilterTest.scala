package markvshaney.internal.text.transforms

import markvshaney.internal.text.WhitespaceTokenizer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DialogueFilterTest extends AnyFlatSpec with Matchers {

  behavior of "transform"

  it should "remove dialogue within quotes" in {
    // Given, When
    val actual = DialogueFilter.transform(toTokenStream("Text \"containing some dialogue\" and more text \"and more dialogue\" and final text"))

    // Then
    actual.mkString(" ") shouldBe "Text and more text and final text"
  }

  private def toTokenStream(s: String): Iterator[String] =
    s.split(" ").iterator
}
