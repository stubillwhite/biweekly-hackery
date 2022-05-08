package markvshaney.internal

package object domain {

  // Markov model types

  type StateIdentifier = String

  trait MarkovModelBuilder {

    def buildModel[A](events: Iterator[A],
                      stateIdentifierGenerator: Seq[A] => StateIdentifier,
                      contextLength: Int): MarkovModel[A]
  }

  trait MarkovModel[A] {

    def getStates(): Set[StateIdentifier]

    def generateEvents(initialState: StateIdentifier): Iterator[A]
  }

  // Text source types

  type TokenStream = Iterator[String]

  trait Tokenizer {
    def tokenize(lines: Iterator[String]): TokenStream
  }

  trait TextTransform {
    val transform: (tokens: TokenStream) => TokenStream
  }
}