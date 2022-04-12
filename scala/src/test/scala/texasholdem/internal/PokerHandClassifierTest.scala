package texasholdem.internal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import texasholdem.internal.domain.Card
import texasholdem.internal.domain.Suits.*
import texasholdem.internal.PokerHandClassifier.PokerHands.*
import texasholdem.internal.PokerHandClassifier.{bestPokerHand, classifyPokerHand}
import texasholdem.internal.testcommon.TestCards

import scala.util.Random.shuffle

class PokerHandClassifierTest extends AnyFlatSpec with Matchers with MockitoSugar with TestCards {

  behavior of "classifyHand"

  it should "order hands of different type based on hand rank" in {
    // Given
    val cards = List(
      shuffledHand(CT, CJ, CQ, CK, CA),
      shuffledHand(C9, CT, CJ, CQ, CK),
      shuffledHand(C9, D9, H9, S9, C8),
      shuffledHand(C9, D9, H9, D8, C8),
      shuffledHand(C2, C3, C9, CQ, CJ),
      shuffledHand(C9, D8, C7, C6, C5),
      shuffledHand(C9, D9, H9, C2, C3),
      shuffledHand(C9, D9, C8, D8, C4),
      shuffledHand(C9, D9, C2, C3, C4),
      shuffledHand(C2, C4, D8, CJ, CA))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      RoyalFlush(Clubs),
      StraightFlush(Clubs, 13),
      FourOfAKind(9, List(8)),
      FullHouse(9, 8),
      Flush(Clubs, List(12, 11, 9, 3, 2)),
      Straight(9),
      ThreeOfAKind(9, List(3, 2)),
      TwoPair(9, 8, List(4)),
      Pair(9, List(4, 3, 2)),
      HighCard(List(14, 11, 8, 4, 2))))
  }

  it should "require A K Q J T suited for a royal flush" in {
    // Given
    val cards = List(
      shuffledHand(CT, CJ, CQ, CK, CA),
      shuffledHand(C3, C4, C5, C6, CA),
      shuffledHand(CT, CJ, CQ, DK, DA))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      RoyalFlush(Clubs),
      Flush(Clubs, List(14, 6, 5, 4, 3)),
      Straight(14)))
  }

  it should "require sequential suited cards for a straight flush" in {
    // Given
    val cards = List(
      shuffledHand(C9, CT, CJ, CQ, CK),
      shuffledHand(CA, C2, C3, C4, C5),
      shuffledHand(C8, C9, CJ, CQ, CK),
      shuffledHand(C9, CT, CJ, DQ, DK))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      StraightFlush(Clubs, 13),
      StraightFlush(Clubs, 5),
      Flush(Clubs, List(13, 12, 11, 9, 8)),
      Straight(13)))
  }

  it should "order straight flush draws based on high card" in {
    // Given
    val cards = List(
      shuffledHand(C9, CT, CJ, CQ, CK),
      shuffledHand(C8, C9, CT, CJ, CQ),
      shuffledHand(CA, C2, C3, C4, C5))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      StraightFlush(Clubs, 13),
      StraightFlush(Clubs, 12),
      StraightFlush(Clubs, 5)))
  }

  it should "require four cards of equal rank for four of a kind" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H9, S9, C6),
      shuffledHand(C8, D8, H8, C7, C6))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      FourOfAKind(9, List(6)),
      ThreeOfAKind(8, List(7, 6))))
  }

  it should "order four of a kind draws based on rank and kickers" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H9, S9, C5),
      shuffledHand(C9, D9, H9, S9, C4),
      shuffledHand(C8, D8, H8, S8, C6))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      FourOfAKind(9, List(5)),
      FourOfAKind(9, List(4)),
      FourOfAKind(8, List(6))))
  }

  it should "required a three cards of one rank and two of another for a full house" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H9, S5, C5),
      shuffledHand(C9, D9, H5, S5, C4))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      FullHouse(9, 5),
      TwoPair(9, 5, List(4))))
  }

  it should "order full house draws based on triplet rank then pair rank" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H9, C8, D8),
      shuffledHand(C8, C8, C8, CA, DA),
      shuffledHand(C8, C8, C8, CK, DK))

    // When
    val classifiedAndOrderedHands = classifyAndOrderHands(cards)

    // Then
    classifiedAndOrderedHands should be (List(
      FullHouse(9, 8),
      FullHouse(8, 14),
      FullHouse(8, 13)))
  }

  it should "require suited cards for a flush" in {
    // Given
    val cards = List(
      shuffledHand(C8, C9, CJ, CQ, CK),
      shuffledHand(C9, CT, CJ, DQ, DK))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      Flush(Clubs, List(13, 12, 11, 9, 8)),
      Straight(13)))
  }

  it should "order flush draws based on card ranks" in {
    // Given
    val cards = List(
      shuffledHand(C9, C8, C7, C6, C4),
      shuffledHand(C9, C8, C7, C5, C4),
      shuffledHand(C9, C8, C7, C5, C3))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      Flush(Clubs, List(9, 8, 7, 6, 4)),
      Flush(Clubs, List(9, 8, 7, 5, 4)),
      Flush(Clubs, List(9, 8, 7, 5, 3))))
  }

  it should "require sequential cards for a straight" in {
    // Given
    val cards = List(
      shuffledHand(C9, DT, HJ, SQ, CK),
      shuffledHand(C2, DT, HJ, HQ, HK))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      Straight(13),
      HighCard(List(13, 12, 11, 10, 2))))
  }

  it should "order straight draws based on high card" in {
    // Given
    val cards = List(
      shuffledHand(D9, C8, C7, C6, C5),
      shuffledHand(D8, C7, C6, C5, C4),
      shuffledHand(D7, C6, C5, C4, C3))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      Straight(9),
      Straight(8),
      Straight(7)))
  }

  it should "require a three cards of equal rank for three of a kind" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H9, C6, C5),
      shuffledHand(C9, D9, H8, C6, C5))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      ThreeOfAKind(9, List(6, 5)),
      Pair(9, List(8, 6, 5))))
  }

  it should "order three of a kind draws based on rank and kickers" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H9, C6, C5),
      shuffledHand(C8, D8, H8, C5, C4),
      shuffledHand(C8, D8, H8, C4, C3))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      ThreeOfAKind(9, List(6, 5)),
      ThreeOfAKind(8, List(5, 4)),
      ThreeOfAKind(8, List(4, 3))))
  }

  it should "require two cards of equal rank and two of another equal rank for a two pair" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H7, S7, C5),
      shuffledHand(C9, D9, H7, S6, C5))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      TwoPair(9, 7, List(5)),
      Pair(9, List(7, 6, 5))))
  }

  it should "order two pair draws based on highest pair rank then second highest then kicker" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, H7, S7, C5),
      shuffledHand(C9, D9, H6, S6, C5),
      shuffledHand(C9, D9, H6, S6, C4))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      TwoPair(9, 7, List(5)),
      TwoPair(9, 6, List(5)),
      TwoPair(9, 6, List(4))))
  }

  it should "require two cards of equal rank for a pair" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, C6, C5, C4),
      shuffledHand(C9, D8, C6, C5, C4))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      Pair(9, List(6, 5, 4)),
      HighCard(List(9, 8, 6, 5, 4))))
  }

  it should "order pair draws based on pair rank then kickers" in {
    // Given
    val cards = List(
      shuffledHand(C9, D9, C6, C5, C4),
      shuffledHand(C8, D8, C6, C5, C4),
      shuffledHand(C8, D8, C6, C5, C3))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      Pair(9, List(6, 5, 4)),
      Pair(8, List(6, 5, 4)),
      Pair(8, List(6, 5, 3))))
  }

  it should "order high card draws based on kickers" in {
    // Given
    val cards = List(
      shuffledHand(C9, D7, C6, C5, C4),
      shuffledHand(C9, D7, C6, C5, C3))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      HighCard(List(9, 7, 6, 5, 4)),
      HighCard(List(9, 7, 6, 5, 3))))
  }

  it should "order hands differing on kickers using all kickers" in {
    val cards = List(
      shuffledHand(CA, DQ, CT, D8, C6),
      shuffledHand(CK, DQ, CT, D8, C6),
      shuffledHand(CK, DJ, CT, D8, C6),
      shuffledHand(CK, DJ, C9, D8, C6),
      shuffledHand(CK, DJ, C9, D7, C6),
      shuffledHand(CK, DJ, C9, D7, C5))

    // When, Then
    classifyAndOrderHands(cards) should be (List(
      HighCard(List(14, 12, 10, 8, 6)),
      HighCard(List(13, 12, 10, 8, 6)),
      HighCard(List(13, 11, 10, 8, 6)),
      HighCard(List(13, 11,  9, 8, 6)),
      HighCard(List(13, 11,  9, 7, 6)),
      HighCard(List(13, 11,  9, 7, 5))))
  }

  private def classifyAndOrderHands(cards: List[List[Card]]) = {
    cards.reverse.map(classifyPokerHand).sorted
  }

  private def shuffledHand(cards: Card*): List[Card] = {
    shuffle(cards).toList
  }
}
