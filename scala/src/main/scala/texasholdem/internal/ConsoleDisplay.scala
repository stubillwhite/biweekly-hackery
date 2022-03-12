package texasholdem.internal

import texasholdem.internal.Combinatorics.selectWithoutReplacement
import texasholdem.internal.PokerHandClassifier.bestPokerHand
import texasholdem.internal.PokerHandClassifier.PokerHand
import texasholdem.internal.PokerHandClassifier.PokerHands.*
import texasholdem.internal.domain.*
import texasholdem.internal.domain.Suits.*

class ConsoleDisplay extends Display {

  override def displayGame(game: Game): Unit = {
    val communityCards = game.communityCards.map(cardToString)

    println(
      s"""Community cards:
         |  Flop:  ${communityCards.slice(0, 3).mkString(" ")}
         |  Turn:  ${communityCards.slice(3, 4).mkString(" ")}
         |  River: ${communityCards.slice(4, 5).mkString(" ")}
         |""".stripMargin)

    println("Player hole cards:")
    game.playOrder.foreach(playerId => {
      val playerHand: String = if (game.communityCards.nonEmpty) {
        s"[${pokerHandToString(bestPokerHand(game.holeCards(playerId), game.communityCards))}]"
      } else {
        ""
      }
      println(s"""  ${playerId}: ${game.holeCards(playerId).map(cardToString).mkString(" ")} \t ${playerHand}""")
    })

    println()
  }

  private def cardToString(card: Card): String = {
    val suitSymbol = card.suit match {
      case Clubs => "\u2663"
      case Diamonds => "\u2666"
      case Hearts => "\u2665"
      case Spades => "\u2664"
    }

    rankToString(card.rank) + suitSymbol
  }

  private def rankToString(rank: Rank): String = {
    "2,3,4,5,6,7,8,9,T,J,Q,K,A".split(',')(rank - 2)
  }

  private def pokerHandToString(pokerHand: PokerHand): String = {
    pokerHand match {
      case RoyalFlush(suit)                    => s"Royal flush (${suit})"
      case StraightFlush(suit, highCard)       => s"Straight flush, ${suit}, ${rankToString(highCard)} high"
      case FourOfAKind(rank, kickers)          => s"Four of kind ${rankToString(rank)}s (${kickersToString(kickers)} kicker)"
      case FullHouse(tripleRank, pairRank)     => s"Full house (${rankToString(tripleRank)}s over ${rankToString(pairRank)}s)"
      case Flush(suit, kickers)                => s"${suit} flush (${kickersToString(kickers)} kickers)"
      case Straight(highCard)                  => s"Straight (${rankToString(highCard)} high)"
      case ThreeOfAKind(rank, kickers)         => s"Three of a kind ${rankToString(rank)}s (${kickersToString(kickers)} kickers)"
      case TwoPair(highRank, lowRank, kickers) => s"Two pair, ${rankToString(highRank)}s and ${rankToString(lowRank)}s (${kickersToString(kickers)} kicker)"
      case Pair(rank, kickers)                 => s"Pair of ${rankToString(rank)}s (${kickersToString(kickers)} kickers)"
      case HighCard(kickers)                   => s"High card (${kickersToString(kickers)} kickers)"
    }
  }

  private def kickersToString(kickers: List[Rank]) = {
    kickers.map(rankToString).mkString(", ")
  }
}
