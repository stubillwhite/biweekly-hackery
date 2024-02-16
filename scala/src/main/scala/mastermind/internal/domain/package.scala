package mastermind.internal

package object domain {

  // Colors

  object Color extends Enumeration {
    type Color = Value
    val Red, Blue, Yellow, Green, White, Black = Value
  }

  // Code

  case class CodePeg(color: Color.Color)

  object CodePegs {
    val Red: CodePeg = CodePeg(Color.Red)
    val Blue: CodePeg = CodePeg(Color.Blue)
    val Yellow: CodePeg = CodePeg(Color.Yellow)
    val Green: CodePeg = CodePeg(Color.Green)
    val White: CodePeg = CodePeg(Color.White)
    val Black: CodePeg = CodePeg(Color.Black)
  }

  private type Code = Seq[CodePeg]
  def Code(codePegs: CodePeg*): Code = Seq(codePegs: _*)

  // Keys

  case class KeyPeg(color: Color.Color)

  object KeyPegs {
    val FullyCorrect: KeyPeg = KeyPeg(Color.Black)
    val PartiallyCorrect: KeyPeg = KeyPeg(Color.White)
  }

  private type Feedback = Seq[KeyPeg]
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
                  code: Code,
                  rounds: Seq[Round])
}
