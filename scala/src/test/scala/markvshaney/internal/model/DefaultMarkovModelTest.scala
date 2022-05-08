package markvshaney.internal.model

import markvshaney.internal.domain.{MarkovModel, StateIdentifier}
import markvshaney.internal.model.RandomNumberProvider
import markvshaney.internal.model.RandomNumberProvider
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DefaultMarkovModelTest extends AnyFlatSpec with Matchers {

  private val alwaysMinRandomNumberProvider = new RandomNumberProvider {
    override def nextInt(n: Int): Int = 0
  }

  private val alwaysMaxRandomNumberProvider = new RandomNumberProvider {
    override def nextInt(n: Int): Int = n - 1
  }

  private val toStateIdentifier = (events: Seq[String]) => events.mkString("_")

  behavior of "DefaultMarkovModel"

  it should "continue until a terminal state" in {
    // Given
    val model = buildMarkovModelUsing(eventStream("initial a b c"))

    // When
    val text = model.generateEvents("initial").mkString(" ")

    // Then
    text shouldBe "a b c"
  }

  it should "select transitions randomly" in {
    // Given
    val inputText = "a b c a b d"
    val startingState = "a"

    val modelBiasedToMin = buildMarkovModelUsing(alwaysMinRandomNumberProvider, eventStream(inputText))
    val modelBiasedToMax = buildMarkovModelUsing(alwaysMaxRandomNumberProvider, eventStream(inputText))

    // When, Then
    modelBiasedToMin.generateEvents(startingState).take(2).mkString(" ") shouldBe "b c"
    modelBiasedToMax.generateEvents(startingState).take(2).mkString(" ") shouldBe "b d"
  }

  private def buildMarkovModelUsing(eventStream: Iterator[String]): MarkovModel[String] = {
    DefaultMarkovModel.buildModel(eventStream, toStateIdentifier, 1)
  }

  private def buildMarkovModelUsing(randomNumberProvider: RandomNumberProvider,
                                    eventStream: Iterator[String]): MarkovModel[String] = {
    DefaultMarkovModel.buildModel(randomNumberProvider, eventStream, toStateIdentifier, 1)
  }

  private def eventStream(s: String): Iterator[String] = {
    s.split(" ").iterator
  }
}
