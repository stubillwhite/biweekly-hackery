package texasholdem.internal

import texasholdem.internal.domain.ActionRound.*
import texasholdem.internal.domain.{Action, Card, Deck, Game, Player, PlayerView, PlayerId, ActionRound}
import texasholdem.internal.PokerHandClassifier.*

object TexasHoldEmRules {

  type RoundHandler = Game => Game

  def play(game: Game): Game = {
    val roundHandlers = List(
      dealCardToPlayers,
      dealCardToPlayers,
      preFlopActions,
      burnCard,
      dealFlop,
      flopActions,
      burnCard,
      dealTurn,
      turnActions,
      burnCard,
      dealRiver,
      riverActions,
      showdown
    )

    roundHandlers.foldLeft(game) {
      case (game, handler) =>
        handler(game)
    }
  }

  private val dealCardToPlayers: RoundHandler = (game: Game) => {
    def dealToPlayer(game: Game, playerId: PlayerId): Game = {
      val (newDeck, cards) = game.deck.deal(1)

      val newPlayerHoleCards = cards ++ game.holeCards(playerId)
      val newHoleCards = game.holeCards + (playerId -> newPlayerHoleCards)

      game.copy(holeCards = newHoleCards, deck = newDeck)
    }

    game.playOrder.foldLeft(game)(dealToPlayer)
  }

  private val burnCard: RoundHandler = (game: Game) => {
    val (newDeck, _) = game.deck.deal(1)
    game.copy(deck = newDeck)
  }

  private val dealFlop: RoundHandler = (game: Game) => {
    dealToCommunityCards(game, 3)
  }

  private val dealTurn: RoundHandler = (game: Game) => {
    dealToCommunityCards(game, 1)
  }

  private val dealRiver: RoundHandler = (game: Game) => {
    dealToCommunityCards(game, 1)
  }

  private val preFlopActions: RoundHandler = (game: Game) => {
    placeholderForPlayerActionHandling(PreFlop, game)
  }

  private val flopActions: RoundHandler = (game: Game) => {
    placeholderForPlayerActionHandling(Flop, game)
  }

  private val turnActions: RoundHandler = (game: Game) => {
    placeholderForPlayerActionHandling(Turn, game)
  }

  private val riverActions: RoundHandler = (game: Game) => {
    placeholderForPlayerActionHandling(River, game)
  }

  private val showdown: RoundHandler = (game: Game) => {
    val playerHands = game.playOrder.map { playerId => (playerId, bestPokerHand(game.holeCards(playerId), game.communityCards)) }.toMap
    game.display.displayShowdown(playerHands)
    game
  }

  private def dealToCommunityCards(game: Game, n: Int): Game = {
    val (newDeck, cards) = game.deck.deal(n)
    val newCommunityCards = game.communityCards ++ cards
    game.copy(deck = newDeck, communityCards = newCommunityCards)
  }

  private def displayState(game: Game): Unit = {
    game.display.displayGame(game)
  }

  private def placeholderForPlayerActionHandling(actionRound: ActionRound, game: Game): Game = {
    def getPlayerAction(playerId: PlayerId): Action = {
      val playerView = PlayerView(actionRound, game.holeCards(playerId), game.communityCards, game.playOrder, Map())
      game.playersById(playerId).getAction(playerView)
    }

    displayState(game)
    game.playOrder.map(getPlayerAction)
    game
  }
}
