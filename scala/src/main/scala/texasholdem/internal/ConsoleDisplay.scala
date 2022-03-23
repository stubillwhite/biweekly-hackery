package texasholdem.internal

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
      println(s"""  ${playerId}: ${game.holeCards(playerId).map(cardToString).mkString(" ")}""")
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
}
