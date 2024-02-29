package mastermind

import mastermind.internal.domain.KeyPegs.*
import mastermind.internal.domain.*
import mastermind.internal.domain.FeedbackCalculator
import mastermind.internal.{CharacterDisplay, ComputerCodeMaker, ComputerFeedbackCalculator, HumanCodeBreaker}

import scala.annotation.tailrec

object Mastermind {

  def main(args: Array[String]): Unit = {
    val codeMaker = new ComputerCodeMaker()
    val codeBreaker = new HumanCodeBreaker()
    val display = new CharacterDisplay()
    val feedbackCalculator = new ComputerFeedbackCalculator()
    val code = codeMaker.makeCode()

    val game = Game(codeMaker, codeBreaker, feedbackCalculator, code, Vector())

    val displayState = (x: Game) => {
      display.displayState(x)
      x
    }

    val states =
      LazyList.iterate(game)(nextState)
        .map(displayState)
        .takeUntil(x => x.isWon() || x.isLost())

    if (states.last.isWon()) {
      println("You win!")
    }
    else {
      println("You lose!")
    }
  }

  private def nextState(game: Game): Game = {
    val guess = game.codeBreaker.guess(game.rounds)
    val feedback = game.calculator.calculateFeedback(game.code, guess)
    game.copy(rounds = game.rounds :+ Round(guess, feedback))
  }

  implicit class TakeUntilWrapper[T](list: LazyList[T]) {
    def takeUntil(predicate: T => Boolean): LazyList[T] = {
      list.span(!predicate(_)) match {
        case (head, tail) => head ++ tail.take(1)
      }
    }
  }
}
