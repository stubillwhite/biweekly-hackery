package cheatinghangman.internal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

class ComputerPlayerTest extends AnyFlatSpec with Matchers with MockitoSugar {

  behavior of "guessCharacter"

  it should "consider only words with the correct length" in {
    // Given
    val player = ComputerPlayer(List("AB", "AC", "XXXX", "XXXX", "XXXX"))

    // When
    val guess = player.guessCharacter(word("- -"), "".toSet, 10)

    guess should be ('A')
  }

  it should "consider only words which match known letters" in {
    // Given
    val player = ComputerPlayer(List("ABC", "ABD", "XXB", "XXB", "XXB"))

    // When
    val guess = player.guessCharacter(word("- B -"), "B".toSet, 10)

    guess should be ('A')
  }

  it should "consider only words which do not match incorrect guesses" in {
    // Given
    val player = ComputerPlayer(List("AAA", "AAA", "XXB", "XXB", "XXB"))

    // When
    val guess = player.guessCharacter(word("- - -"), "X".toSet, 10)

    guess should be ('A')
  }

  it should "guess the letter occurring in the most words ignoring repetition within the word" in {
    // Given
    val player = ComputerPlayer(List("ABC", "ADE", "AXX", "XXX"))

    // When
    val guess = player.guessCharacter(word("- - -"), "".toSet, 10)

    guess should be ('A')
  }

  private def word(s: String): List[Option[Char]] = {
    s.replaceAll(" ", "")
      .map(x => if (x == '-') None else Some(x)).toList
  }
}
