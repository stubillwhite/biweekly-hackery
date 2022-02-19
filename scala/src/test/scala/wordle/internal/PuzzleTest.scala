package wordle.internal

import org.mockito.ArgumentMatchers.any
import wordle.internal.Puzzle
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import wordle.internal.domain.{Correct, Display, GuessFeedback, InProgress, IncorrectLetter, IncorrectLocation, LetterFeedback, Lost, Player, Won}

class PuzzleTest extends AnyFlatSpec with MockitoSugar with Matchers with BeforeAndAfterEach {

  private val mockPlayer: Player = mock[Player]

  override protected def beforeEach(): Unit = {
    reset(mockPlayer)
  }

  behavior of "createPuzzle"

  it should "create a puzzle with the initial state" in {
    // Given
    val wordList = List("abcde", "bcdef", "invalid-word")

    // When
    val actual = Puzzle.newPuzzle(wordList, mockPlayer)

    // Then
    actual match {
      case Puzzle(wordList, player, word, puzzleStatus, guessFeedback) =>
        wordList shouldBe List("ABCDE", "BCDEF")
        player shouldBe mockPlayer
        List("ABCDE", "BCDEF") should contain (word)
        puzzleStatus shouldBe InProgress
        guessFeedback shouldBe List()
    }
  }

  behavior of "nextState"

  it should "return feedback for letter locations" in {
    // Given
    val puzzle = createPuzzle(List("abcde", "axyzb"), "abcde")
    when(mockPlayer.guessWord(any(classOf[Seq[GuessFeedback]]))).thenReturn("axyzb")

    val expectedLetterFeedback = Seq(
      LetterFeedback('A', Correct),
      LetterFeedback('X', IncorrectLetter),
      LetterFeedback('Y', IncorrectLetter),
      LetterFeedback('Z', IncorrectLetter),
      LetterFeedback('B', IncorrectLocation))

    // When
    val actual = puzzle.nextState()

    actual.guessFeedback.length shouldBe 1
    actual.guessFeedback.head.letterFeedback shouldBe expectedLetterFeedback
  }

  it should "lose a game with six failed guesses" in {
    // Given
    val puzzle = createPuzzle(List("abcde", "axyzb"), "abcde")
    when(mockPlayer.guessWord(any(classOf[Seq[GuessFeedback]]))).thenReturn("axyzb")

    // When
    val actual = puzzle.nextState().nextState().nextState().nextState().nextState().nextState()

    // Then
    actual.puzzleStatus shouldBe Lost
  }

  it should "win a game with a correct guess" in {
    // Given
    val puzzle = createPuzzle(List("abcde", "axyzb"), "abcde")
    when(mockPlayer.guessWord(any(classOf[Seq[GuessFeedback]]))).thenReturn("abcde")

    // When
    val actual = puzzle.nextState()

    // Then
    actual.puzzleStatus shouldBe Won
  }

  it should "win a game with a correct guess in the final turn" in {
    // Given
    val puzzle = createPuzzle(List("abcde", "axyzb"), "abcde")
    when(mockPlayer.guessWord(any(classOf[Seq[GuessFeedback]])))
      .thenReturn("axyzb")
      .thenReturn("axyzb")
      .thenReturn("axyzb")
      .thenReturn("axyzb")
      .thenReturn("axyzb")
      .thenReturn("abcde")

    // When
    val actual = puzzle.nextState().nextState().nextState().nextState().nextState().nextState()

    // Then
    actual.puzzleStatus shouldBe Won
  }

  private def createPuzzle(wordList: List[String], word: String): Puzzle = {
    Puzzle.newPuzzle(wordList, mockPlayer).copy(word = word.toUpperCase)
  }
}
