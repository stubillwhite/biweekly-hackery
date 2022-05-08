package markvshaney

import markvshaney.internal.domain.*
import markvshaney.internal.text.GutenburgTextResource
import markvshaney.internal.text.WhitespaceTokenizer
import markvshaney.internal.text.transforms.*
import markvshaney.internal.model.DefaultMarkovModel

import scala.util.Random

import scala.io.Source

object MarkVShaney {

  def main(args: Array[String]): Unit = {
    val sources = Iterator(
      "/markvshaney/right-ho-jeeves.txt",
      "/markvshaney/the-dunwich-horror.txt",
      "/markvshaney/the-king-in-yellow.txt",
      "/markvshaney/the-shunned-house.txt",
    )

    val transformChain = Iterator(
      PunctuationNormalizer,
      HonorificNormalizer,
      DialogueFilter
    )

    val transform = transformChain.foldLeft(identity[Iterator[String]]) { case (acc, x: TextTransform) => acc.andThen(x.transform) }

    val tokens = sources
      .map(GutenburgTextResource.readLines)
      .map(WhitespaceTokenizer.tokenize)
      .flatMap(transform)

    val stateToString = (tokens: Seq[String]) => tokens.mkString("_")

    val model = DefaultMarkovModel.buildModel(tokens, stateToString, 2)

    val text = generateText(model, 50)
    println(text)
  }

  private def generateText(model: MarkovModel[String], minLength: Int) = {
    val initialState = Random.shuffle(model.getStates().toList).head
    val events = model.generateEvents(initialState)

    val lengthTrimmer = new LengthTrimmer(minLength)
    lengthTrimmer.transform(events).mkString(" ")
  }
}
