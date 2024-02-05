package texasholdem.internal

import texasholdem.internal.PokerHandClassifier.PokerHand

import scala.util.Random

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

  trait Deck {
    def shuffle(): Deck
    def deal(n: Int): (Deck, List[Card])
  }

  sealed trait Action

  object Action {
    case object Fold extends Action
    case object Check extends Action
    case class Bet(amount: Int) extends Action
    case object Call extends Action
    case class Raise(amount: Int) extends Action
  }

  sealed trait ActionRound

  object ActionRound {
    case object PreFlop extends ActionRound
    case object Flop extends ActionRound
    case object Turn extends ActionRound
    case object River extends ActionRound
  }

  type PlayerId = String

  case class PlayerView(actionRound: ActionRound,
                        holeCards: List[Card],
                        communityCards: List[Card],
                        playOrder: List[PlayerId],
                        playerActions: Map[PlayerId, List[Action]])

  trait Player {
    def id: PlayerId
    def getAction(playerView: PlayerView): Action
  }

  trait Display {
    def displayGame(game: Game): Unit
    def displayLastPlayerActions(game: Game): Unit
    def displayShowdown(playerHands: Map[PlayerId, PokerHand]): Unit
  }

  case class Game(deck: Deck,
                  holeCards: Map[PlayerId, List[Card]],
                  communityCards: List[Card],
                  playOrder: List[PlayerId],
                  playersById: Map[PlayerId, Player],
                  playerActions: Map[PlayerId, List[Action]],
                  display: Display)
}
