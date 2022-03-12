package texasholdem.internal

import texasholdem.internal.Combinatorics.selectWithoutReplacement
import texasholdem.internal.domain.{Card, Deck, Rank, Suit}

import scala.annotation.tailrec

object PokerHandClassifier {

  def bestPokerHand(holeCards: List[Card], communityCards: List[Card]): PokerHand = {
    selectWithoutReplacement(5, holeCards ++ communityCards)
      .map(classifyPokerHand)
      .min
  }

  def classifyPokerHand(cards: List[Card]): PokerHand = {
    matchSuitBasedHands(cards)
      .getOrElse(matchRankBasedHands(cards)
        .getOrElse(matchHighCardHands(cards)))
  }

  sealed trait PokerHand extends Ordered[PokerHand] {
    // The rank of the hand relative to other PokerHands, with a higher value indicating a stronger hand.
    val handRank: Int

    // The ranks of the cards which play a part in tie-breaking when comparing this PokerHand to another PokerHand with the same handRank.
    val cardRanks: List[Rank]

    override def compare(that: PokerHand): Int = {
      that.handRank.compareTo(this.handRank) match {
        case 0 =>
          compareCardRanks(that.cardRanks, this.cardRanks)
        case x =>
          x
      }
    }

    @tailrec
    private def compareCardRanks(a: List[Rank], b: List[Rank]): Int = {
      if (a.isEmpty) {
        0
      } else {
        a.head.compareTo(b.head) match {
          case 0 => compareCardRanks(a.tail, b.tail)
          case x => x
        }
      }
    }
  }

  object PokerHands {

    case class RoyalFlush(suit: Suit) extends PokerHand {
      override val handRank: Int = 10
      override val cardRanks: List[Rank] = Nil
    }

    case class StraightFlush(suit: Suit, highCard: Int) extends PokerHand {
      override val handRank: Int = 9
      override val cardRanks: List[Rank] = highCard :: Nil
    }

    case class FourOfAKind(rank: Rank, kickers: List[Rank]) extends PokerHand {
      override val handRank: Int = 8
      override val cardRanks: List[Rank] = rank :: kickers
    }

    case class FullHouse(threeOfAKindRank: Rank, pairRank: Rank) extends PokerHand {
      override val handRank: Int = 7
      override val cardRanks: List[Rank] = threeOfAKindRank :: pairRank :: Nil
    }

    case class Flush(suit: Suit, kickers: List[Rank]) extends PokerHand {
      override val handRank: Int = 6
      override val cardRanks: List[Rank] = kickers
    }

    case class Straight(highCard: Rank) extends PokerHand {
      override val handRank: Int = 5
      override val cardRanks: List[Rank] = highCard :: Nil
    }

    case class ThreeOfAKind(rank: Rank, kickers: List[Rank]) extends PokerHand {
      override val handRank: Int = 4
      override val cardRanks: List[Rank] = rank :: kickers
    }

    case class TwoPair(highRank: Rank, lowRank: Rank, kickers: List[Rank]) extends PokerHand {
      override val handRank: Int = 3
      override val cardRanks: List[Rank] = highRank :: lowRank :: kickers
    }

    case class Pair(rank: Rank, kickers: List[Rank]) extends PokerHand {
      override val handRank: Int = 2
      override val cardRanks: List[Rank] = rank :: kickers
    }

    case class HighCard(kickers: List[Rank]) extends PokerHand {
      override val handRank: Int = 1
      override val cardRanks: List[Rank] = kickers
    }
  }

  import PokerHands._

  private def matchRankBasedHands(cards: List[Card]): Option[PokerHand] = {
    // Group cards by rank, ordered by size of group then by rank
    // (C3, D3, C2, D2, H2) => ((C2, D2, H2), (C3, D3))
    val groupedByRank = cards
      .groupBy(_.rank)
      .values
      .toList
      .sortBy(x => (x.size, x.head.rank)).reverse

    groupedByRank match {
      case List(a, _, _, _) :: kickers => Some(FourOfAKind(a.rank, kickers.flatten.map(_.rank)))
      case List(a, _, _) :: List(b, _) :: Nil => Some(FullHouse(a.rank, b.rank))
      case List(Card(highRank, _)) :: _ :: _ :: _ :: List(Card(lowRank, _)) :: Nil if (highRank - lowRank == 4) => Some(Straight(highRank))
      case List(a, _, _) :: kickers => Some(ThreeOfAKind(a.rank, kickers.flatten.map(_.rank)))
      case List(a, _) :: List(b, _) :: kickers => Some(TwoPair(a.rank, b.rank, kickers.flatten.map(_.rank)))
      case List(a, _) :: kickers => Some(Pair(a.rank, kickers.flatten.map(_.rank)))
      case _ => None
    }
  }

  private def matchSuitBasedHands(cards: List[Card]): Option[PokerHand] = {
    // Group cards by suit, ordered by size of group, and ordered within each group by rank
    // (C2, C3, C4, D2, D3) => ((C4, C3, C2), (D3, D2))
    val groupedBySuit = cards
      .groupBy(_.suit)
      .values
      .toList
      .sortBy(_.size).reverse
      .map(x => x.sortBy(_.rank).reverse)

    groupedBySuit match {
      case List(Card(14, suit), _, _, _, Card(10, _)) :: Nil => Some(RoyalFlush(suit))
      case List(Card(highRank, suit), _, _, _, Card(lowRank, _)) :: Nil if (highRank - lowRank == 4) => Some(StraightFlush(suit, highRank))
      case List(Card(14, suit), Card(5, _), _, _, Card(2, _)) :: Nil => Some(StraightFlush(suit, 5))
      case List(Card(_, suit), _, _, _, _) :: Nil => Some(Flush(suit, cards.map(_.rank).sorted.reverse))
      case _ => None
    }
  }

  private def matchHighCardHands(cards: List[Card]): PokerHand = {
    HighCard(cards.map(_.rank).sorted.reverse)
  }
}
