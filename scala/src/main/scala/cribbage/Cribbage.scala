package cribbage

import cribbage.internal.HandScorer.scoreHand
import cribbage.internal.domain.*

object Cribbage {

  def main(args: Array[String]): Unit = {
    val deck = Deck().shuffle()

    val (newDeck, playerCards) = deck.deal(5)

    val hiddenCards = playerCards.take(4)
    val faceUpCard = playerCards.last

    val hand = Hand(hiddenCards, faceUpCard)

    println(s"Hand: ${hand}, score: ${scoreHand(hand)}")
  }
}
