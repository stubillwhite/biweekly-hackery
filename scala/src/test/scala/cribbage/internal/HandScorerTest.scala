package cribbage.internal

import cribbage.internal.HandScorer.{scoreFifteens, scoreFlushes, scoreHand, scoreNobs, scorePairs, scoreRuns}
import cribbage.internal.domain.Suits.*
import cribbage.internal.domain.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers;

class HandScorerTest extends AnyFlatSpec with Matchers {

  behavior of "scoreFifteens"

  it should "return zero given none exist" in {
    scoreFifteens(toHand("AC,AD,AH,AS,2C")) shouldBe 0
  }

  it should "return eight given four exist" in {
    scoreFifteens(toHand("TD,6C,4S,5H,5D")) shouldBe 8
  }

  behavior of "scoreRuns"

  it should "return zero given none exist" in {
    scoreRuns(toHand("AC,AD,AS,AH,2C")) shouldBe 0
  }

  it should "return three given runs of three exist" in {
    scoreRuns(toHand("AC,AD,2S,3H,KC")) shouldBe 3
  }

  it should "return four given runs of four exist" in {
    scoreRuns(toHand("AC,AD,2S,3H,4C")) shouldBe 4
  }

  it should "return five given runs of five exist" in {
    scoreRuns(toHand("AC,2D,3S,4H,5C")) shouldBe 5
  }

  behavior of "scorePairs"

  it should "return zero given none exist" in {
    scorePairs(toHand("AC,2C,3C,4C,5C")) shouldBe 0
  }

  it should "return four given two pairs exist" in {
    scorePairs(toHand("AC,AD,2C,2D,3C")) shouldBe 4
  }

  it should "return six given royal pair exists" in {
    scorePairs(toHand("AC,AD,AH,2C,3C")) shouldBe 6
  }

  it should "return twelve given double royal pair exists" in {
    scorePairs(toHand("AC,AD,AH,AS,2C")) shouldBe 12
  }

  behavior of "scoreFlushes"

  it should "return zero given none exist" in {
    scoreFlushes(toHand("AC,AD,AH,AS,2C")) shouldBe 0
  }

  it should "return four given four card flush exists" in {
    scoreFlushes(toHand("AC,2C,3C,4C,AD")) shouldBe 4
  }

  it should "return zero given four card flush exists using face-up card" in {
    scoreFlushes(toHand("AC,2C,3C,AD,4C")) shouldBe 0
  }

  behavior of "scoreNobs"

  it should "return zero given none exist" in {
    scoreNobs(toHand("AC,JD,JH,JS,AC")) shouldBe 0
  }

  it should "return one given one exists" in {
    scoreNobs(toHand("JC,JD,JH,JS,AC")) shouldBe 1
  }

  behavior of "scoreHand"

  it should "return expected value given example hand" in {

    val assertExampleHandHasScore = (exampleHand: String, expectedValue: Int) => {
      scoreHand(toHand(exampleHand)) shouldBe expectedValue
    }

    assertExampleHandHasScore("5D,QS,JC,KH,AC", 10)
    assertExampleHandHasScore("8C,AD,TC,6H,7S", 7)
    assertExampleHandHasScore("AC,6D,5C,TC,8C", 4)
  }

  private def toHand(handStr: String): Hand = {
    val cards = handStr.split(",").map(toCard).toList
    Hand(cards.take(4), cards(4));
  }

  private def toCard(cardStr: String): Card = {
    cardStr.toSeq.map(_.toString) match {
      case Seq(rank, suit) => Card(toRank(rank), toSuit(suit))
    }
  }

  private def toRank(rankStr: String): Int = {
    rankStr match {
      case "A" => 1
      case "T" => 10
      case "J" => 11
      case "Q" => 12
      case "K" => 13
      case num => num.toInt
    }
  }

  private def toSuit(suitStr: String): Suit = {
    suitStr match {
      case "C" => Clubs
      case "D" => Diamonds
      case "H" => Hearts
      case "S" => Spades
    }
  }
}