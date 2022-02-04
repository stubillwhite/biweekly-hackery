package wordle.internal

package object domain {

  sealed trait Feedback

  case object Correct extends Feedback
  case object IncorrectLocation extends Feedback
  case object IncorrectLetter extends Feedback

  case class LetterFeedback(letter: Char, feedback: Feedback)
  case class GuessFeedback(letterFeedback: Seq[LetterFeedback])

  sealed trait PuzzleStatus

  case object InProgress extends PuzzleStatus
  case object Won extends PuzzleStatus
  case object Lost extends PuzzleStatus

  trait Player {
    def guessWord(guessFeedback: Seq[GuessFeedback]): String
  }

  trait Display {
    def display(puzzle: Puzzle): Unit
  }
}
