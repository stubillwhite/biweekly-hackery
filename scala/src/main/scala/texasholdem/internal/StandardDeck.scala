package texasholdem.internal

import texasholdem.internal.domain.Suits.{Clubs, Diamonds, Hearts, Spades}
import texasholdem.internal.domain.{Card, Deck}

import scala.util.Random

object StandardDeck {

  def apply(): StandardDeck = {
    val cards = {
      for {suit <- List(Clubs, Diamonds, Hearts, Spades)
           rank <- 2 to 14}
      yield Card(rank, suit)
    }

    new StandardDeck(cards)
  }
}

class StandardDeck(val cards: List[Card]) extends Deck {
  def shuffle(): Deck = {
    new StandardDeck(Random.shuffle(cards))
  }

  def deal(n: Int): (Deck, List[Card]) = {
    (new StandardDeck(cards.drop(n)), cards.take(n))
  }
}
