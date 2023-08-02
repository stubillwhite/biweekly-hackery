package cribbage.internal

import scala.util.Random

import domain.Suits._

package object domain {

  type Rank = Int

  sealed trait Suit

  object Suits {
    case object Clubs extends Suit
    case object Diamonds extends Suit
    case object Hearts extends Suit
    case object Spades extends Suit
  }

  case class Card(rank: Rank, suit: Suit)

  case class Deck(cards: List[Card]) {
    def shuffle(): Deck = {
      Deck(Random.shuffle(cards))
    }

    def deal(n: Int): (Deck, List[Card]) = {
      (Deck(cards.drop(n)), cards.take(n))
    }
  }

  object Deck {
    def apply(): Deck = {
      val cards = for {
        rank <- Range.inclusive(1, 13)
        suit <- Set(Clubs, Diamonds, Hearts, Spades)
      } yield Card(rank, suit)

      Deck(cards.toList)
    }
  }

  case class Hand(cards: List[Card], faceUpCard: Card) {
    val allCards: List[Card] = faceUpCard :: cards
  }
}
