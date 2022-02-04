package wordle.internal

import wordle.internal.domain.*
import wordle.internal.TextDisplay.*

object TextDisplay {
  private val ANSI_RESET = "\u001B[0m"
  private val ANSI_BLACK = "\u001B[30m"
  private val ANSI_RED_BACKGROUND = "\u001B[41m"
  private val ANSI_GREEN_BACKGROUND = "\u001B[42m"
  private val ANSI_YELLOW_BACKGROUND = "\u001B[43m"

  private val ALPHABET: String =
    """Q W E R T Y U I O P
      | A S D F G H J K L
      |   Z X C V B N M""".stripMargin

  def apply(): TextDisplay = {
    new TextDisplay()
  }
}

class TextDisplay extends Display {

  override def display(puzzle: Puzzle): Unit = {
    println
    println(renderGuessFeedback(puzzle.guessFeedback))
    println
    println(renderAlphabet(puzzle.guessFeedback))
    println

    puzzle.puzzleStatus match {
      case Won => println("You win!")
      case Lost => println(s"Sorry! The word was ${puzzle.word}. Better luck next time!")
      case _ =>
    }
  }

  private def renderGuessFeedback(guessFeedback: Seq[GuessFeedback]): String = {
    guessFeedback
      .map(x => x.letterFeedback.map(renderLetter).mkString(" "))
      .mkString("\n\n")
  }

  private def renderAlphabet(guessFeedback: Seq[GuessFeedback]): String = {
    val feedbackForGuessedLetters =
      guessFeedback
        .flatMap(_.letterFeedback)
        .groupBy(_.letter)
        .map { case (k, vs) => (k, bestLetterStatus(vs.map(_.feedback).toSet)) }

    feedbackForGuessedLetters.foldLeft(ALPHABET) {
      case (s, (letter, status)) =>
        s.replace(letter.toString, renderLetter(LetterFeedback(letter, status)))
    }
  }

  private def bestLetterStatus(letterStatuses: Set[Feedback]): Feedback = {
    if (letterStatuses.contains(Correct)) Correct
    else if (letterStatuses.contains(IncorrectLocation)) IncorrectLocation
    else IncorrectLetter
  }

  private def renderLetter(letterFeedback: LetterFeedback): String = {
    val backgroundColor = letterFeedback.feedback match {
      case Correct => ANSI_GREEN_BACKGROUND
      case IncorrectLocation => ANSI_YELLOW_BACKGROUND
      case IncorrectLetter => ANSI_RED_BACKGROUND
    }

    s"${ANSI_BLACK}${backgroundColor}${letterFeedback.letter.toString}${ANSI_RESET}"
  }
}
