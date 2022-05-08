package texasholdem.internal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import texasholdem.internal.Combinatorics.*

class CombinatoricsTest extends AnyFlatSpec with Matchers {

  behavior of "selectWithoutReplacement"

  it should "generate all ways of selecting N items from M without replacement" in {
    // Given, When
    val result = selectWithoutReplacement(2, (1 to 3).toList)

    // Then
    result should contain theSameElementsAs List(List(1, 2), List(2, 3), List(1, 3))
  }
}
