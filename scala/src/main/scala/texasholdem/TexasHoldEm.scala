package texasholdem

import texasholdem.internal.domain.ActionRound
import texasholdem.internal.domain.ActionRound.*
import texasholdem.internal.domain.Game
import texasholdem.internal.{ComputerPlayer, ConsoleDisplay, TexasHoldEmRules, StandardDeck}

object TexasHoldEm {

  def main(args: Array[String]): Unit = {
    val players = List(
      ComputerPlayer("player-a"),
      ComputerPlayer("player-b"),
      ComputerPlayer("player-c"),
      ComputerPlayer("player-d"))

    val game = createNewGame(players)

    TexasHoldEmRules.play(game)
  }

  private def createNewGame(players: List[ComputerPlayer]) = {
    val shuffledDeck = StandardDeck().shuffle()

    val holeCards = players.map(_.id -> List()).toMap
    val communityCards = List()
    val playOrder = players.map(_.id)
    val playersById = players.map(player => player.id -> player).toMap
    val playerActions = players.map(player => player.id -> List()).toMap
    val display = new ConsoleDisplay()

    Game(shuffledDeck, holeCards, communityCards, playOrder, playersById, playerActions, display)
  }
}
