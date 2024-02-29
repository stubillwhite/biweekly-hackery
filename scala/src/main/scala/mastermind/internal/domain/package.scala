package mastermind.internal

import mastermind.internal.domain.Color.Color
import mastermind.internal.domain.KeyPegs.{FullyCorrect, PartiallyCorrect}

package object domain {

  // Colors

  object Color extends Enumeration {
    type Color = Value
    val Red, Blue, Yellow, Green, White, Black = Value
  }

  // Code

  case class CodePeg(color: Color)

  object CodePegs {
    val Red: CodePeg = CodePeg(Color.Red)
    val Blue: CodePeg = CodePeg(Color.Blue)
    val Yellow: CodePeg = CodePeg(Color.Yellow)
    val Green: CodePeg = CodePeg(Color.Green)
    val White: CodePeg = CodePeg(Color.White)
    val Black: CodePeg = CodePeg(Color.Black)
  }

  type Code = Seq[CodePeg]
  def Code(codePegs: CodePeg*): Code = Seq(codePegs: _*)

  // Keys

  case class KeyPeg(color: Color)

  object KeyPegs {
    val FullyCorrect: KeyPeg = KeyPeg(Color.Black)
    val PartiallyCorrect: KeyPeg = KeyPeg(Color.White)
  }

  type Feedback = Seq[KeyPeg]
  def Feedback(keyPegs: KeyPeg*): Feedback = Seq(keyPegs: _*)

  trait FeedbackCalculator {
    def calculateFeedback(code: Code, guess: Code): Feedback
  }

  trait CodeMaker {
    def makeCode(): Code
  }

  trait CodeBreaker {
    def guess(previousGuesses: Seq[Round]): Code
  }

  case class Round(guess: Code, feedback: Feedback)

  case class Game(codeMaker: CodeMaker,
                  codeBreaker: CodeBreaker,
                  calculator: FeedbackCalculator,
                  code: Code,
                  rounds: Seq[Round]) {
    
    private val fullyCorrect = Feedback(List.fill(code.length)(FullyCorrect): _*)

    def isWon(): Boolean = {
      rounds.nonEmpty && rounds.last.feedback == fullyCorrect
    }

    def isLost(): Boolean = {
      rounds.length == 10
    }
  }

  trait Display {
    def displayState(game: Game): Unit
  }
}
