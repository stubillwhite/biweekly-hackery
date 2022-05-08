package markvshaney.internal.text.transforms

import markvshaney.internal.domain.TokenStream
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LengthTrimmerTest extends AnyFlatSpec with Matchers {

  behavior of "transform"

  it should "trim the token stream so it begins with the first capitalized word encountered" in {
    // Given, When
    val trimmedText = LengthTrimmer(10).transform(toTokenStream("a b c D e f G"))

    trimmedText.toSeq.head shouldBe "D"
  }

  it should "trim the token stream so it ends with a terminal character and is of at least the minimum length" in {
    // Given, When
    val trimmedText = LengthTrimmer(5).transform(toTokenStream("a b c D e f. G h i j k l. m n o p"))

    trimmedText.mkString(" ") shouldBe "D e f. G h i j k l."
  }

  private def toTokenStream(s: String): TokenStream =
    s.split(" ").iterator
}
