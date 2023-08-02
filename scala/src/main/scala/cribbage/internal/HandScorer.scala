package cribbage.internal

import cribbage.internal.domain.{Card, Hand}

object HandScorer {

  def scoreHand(hand: Hand): Long = {
    scoreFifteens(hand) +
      scoreRuns(hand) + 
      scorePairs(hand) +
      scoreFlushes(hand) +
      scoreNobs(hand)
  }

  private[internal] def scoreFifteens(hand: Hand): Long = {
    val allPermutations =
      Range(1, hand.allCards.size)
        .flatMap(n => selectNFromList(n, hand.allCards))

    allPermutations.count(isSumOfPointsFifteen) * 2
  }

  private[internal] def scoreRuns(hand: Hand): Long = {
    val cardSets = Range.inclusive(3, hand.allCards.size)
      .flatMap(n => selectNFromList(n, hand.allCards))

    cardSets.filter(isRun).maxByOption(_.size) match {
      case Some(run) => run.size
      case None => 0
    }
  }

  private[internal] def scorePairs(hand: Hand): Long = {
    val groupedByRank = hand.allCards.groupBy(_.rank)

    val countNOfAKind = (n: Int) => groupedByRank.values.count(cards => cards.length == n)

    val pairs = countNOfAKind(2)
    val royalPairs = countNOfAKind(3)
    val doubleRoyalPairs = countNOfAKind(4)

    pairs * 2 +
      royalPairs * 6 +
      doubleRoyalPairs * 12
  }

  private[internal] def scoreFlushes(hand: Hand): Long = {
    val allCardsBySuit = hand.allCards.groupBy(_.suit)
    val hasFiveCardFlush = allCardsBySuit.values.exists(cards => cards.length == 5)

    val handCardsBySuit = hand.cards.groupBy(_.suit)
    val hasFourCardFlush = handCardsBySuit.values.exists(cards => cards.length == 4)

    if (hasFiveCardFlush) 5
    else if (hasFourCardFlush) 4
    else 0
  }
  
  private[internal] def scoreNobs(hand: Hand): Long = {
    hand.cards
      .count(card => card.rank == 11 && card.suit == hand.faceUpCard.suit)
  }

  private def selectNFromList[A](n: Int, items: List[A]): List[List[A]] = {
    if (n == 0) List(List())
    else if (n == items.length) List(items)
    else selectNFromList(n - 1, items.tail).map(items.head :: _) ++ selectNFromList(n, items.tail)
  }

  private def isSumOfPointsFifteen(cards: List[Card]): Boolean = {
    val points = (card: Card) => Math.min(card.rank, 10)
    cards.map(points).sum == 15
  }

  private def isRun(cards: List[Card]): Boolean = {
    val sortedByRank = cards.sortBy(_.rank)
    sortedByRank(cards.length - 1).rank - sortedByRank(0).rank == cards.length - 1
  }
}
