package mastermind.internal

import mastermind.internal.domain.*

class CharacterDisplay extends Display {

  override def displayState(game: Game): Unit = {
    println
    println(s"Secret code : ${codeToString(game.code)}")
    game.rounds.foreach(round => {
      println(s"Guess       : ${codeToString(round.guess)} | ${feedbackToString(round.feedback)}")
    })
    println
  }

  private def feedbackToString(feedback: Feedback): String = {
    feedback
      .map(_.color.toString)
      .mkString(" ")
  }

  private def codeToString(code: Code): String = {
    code
      .map(_.color.toString)
      .mkString(" ")
  }
}
