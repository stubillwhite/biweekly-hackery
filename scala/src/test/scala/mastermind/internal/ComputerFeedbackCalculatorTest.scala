package mastermind.internal

import mastermind.internal.domain.CodePegs.*
import mastermind.internal.domain.KeyPegs.*
import mastermind.internal.domain.{Code, Feedback, FeedbackCalculator}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import texasholdem.internal.Combinatorics.*

class ComputerFeedbackCalculatorTest extends AnyFlatSpec with Matchers {

  private val calculator = new ComputerFeedbackCalculator()
  
  behavior of "calculateFeedback"

  it should "give feedback for completely incorrect pins" in {
    calculator.calculateFeedback(Code(Red, White), Code(Yellow, Blue)) should be(Feedback())
  }

  it should "give feedback for single partially correct pin" in {
    calculator.calculateFeedback(Code(Red, White), Code(Yellow, Red)) should be(Feedback(PartiallyCorrect))
  }

  it should "give feedback for multiple partially correct pins" in {
    calculator.calculateFeedback(Code(Red, White), Code(White, Red)) should be(Feedback(PartiallyCorrect, PartiallyCorrect))
  }

  it should "give feedback for single fully correct pin" in {
    calculator.calculateFeedback(Code(Red, White), Code(Red, Blue)) should be(Feedback(FullyCorrect))
  }

  it should "give feedback for multiple fully correct pins" in {
    calculator.calculateFeedback(Code(Red, White), Code(Red, White)) should be(Feedback(FullyCorrect, FullyCorrect))
  }

  it should "give feedback for fully and partially correct pins" in {
    calculator.calculateFeedback(Code(Red, White, Blue), Code(Red, Black, White)) should be(Feedback(FullyCorrect, PartiallyCorrect))
  }

  it should "give feedback for duplicate pins" in {
    calculator.calculateFeedback(Code(Red, Red, Blue, Blue), Code(Red, Red, Red, Blue)) should be(Feedback(FullyCorrect, FullyCorrect, FullyCorrect))
  }
}
