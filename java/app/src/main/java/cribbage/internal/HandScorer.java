package cribbage.internal;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class HandScorer {

    public long scoreHand(Hand hand) {
        return scoreFifteens(hand) +
                scoreRuns(hand) +
                scorePairs(hand) +
                scoreFlushes(hand) +
                scoreNobs(hand);
    }

    long scoreFifteens(Hand hand) {
        final List<Card> allCards = hand.getAllCards();

        final List<List<Card>> allPossibleSetsOfCards =
                IntStream.range(1, allCards.size())
                        .boxed()
                        .flatMap(x -> selectNFromList(x, allCards).stream())
                        .collect(toList());

        final long totalNumberOfFifteens = allPossibleSetsOfCards.stream()
                .filter(this::isSumOfPointsFifteen)
                .count();

        return totalNumberOfFifteens * 2;
    }

    int scoreRuns(Hand hand) {
        final List<Card> allCards = hand.getAllCards();

        final List<List<Card>> allPossibleSetsOfCards =
                IntStream.rangeClosed(3, allCards.size())
                        .boxed()
                        .flatMap(x -> selectNFromList(x, allCards).stream())
                        .collect(toList());

        final List<List<Card>> allRuns = allPossibleSetsOfCards.stream()
                .filter(this::isConsecutive)
                .collect(toList());

        final OptionalInt longestPossibleRun = allRuns.stream()
                .mapToInt(List::size)
                .max();

        if (longestPossibleRun.isPresent()) {
            return longestPossibleRun.getAsInt();
        }
        else {
            return 0;
        }
    }

    long scorePairs(Hand hand) {
        final Map<Rank, List<Card>> groupedByRank = hand.getAllCards().stream().collect(groupingBy(Card::getRank));

        final long totalNumberOfTwoOfAKind = groupedByRank.values().stream()
                .filter(cards -> cards.size() == 2)
                .count();

        final long totalNumberOfThreeOfAKind = groupedByRank.values().stream()
                .filter(cards -> cards.size() == 3)
                .count();

        final long totalNumberOfFourOfAKind = groupedByRank.values().stream()
                .filter(cards -> cards.size() == 4)
                .count();

        return totalNumberOfTwoOfAKind * 2 +
                totalNumberOfThreeOfAKind * 6 +
                totalNumberOfFourOfAKind * 12;
    }

    long scoreFlushes(Hand hand) {
        // Count five card flushes
        final Map<Suit, List<Card>> allCardsBySuit =
                hand.getAllCards().stream().collect(groupingBy(Card::getSuit));

        final long numberOfFiveCardFlushes = allCardsBySuit.values().stream()
                .filter(cards -> cards.size() == 5)
                .count();

        // Count four card flushes
        final Map<Suit, List<Card>> handBySuit =
                hand.getCards().stream().collect(groupingBy(Card::getSuit));

        final long numberOfFourCardFlushes = handBySuit.values().stream()
                .filter(cards -> cards.size() == 4)
                .count();

        if (numberOfFiveCardFlushes == 1) {
            return 5;
        }
        else if (numberOfFourCardFlushes == 1) {
            return 4;
        }
        else {
            return 0;
        }
    }

    long scoreNobs(Hand hand) {
        final long totalNumberOfNobs = hand.getCards().stream()
                .filter(x -> x.getRank() == Rank.Jack && x.getSuit() == hand.getFaceUpCard().getSuit())
                .count();

        return totalNumberOfNobs;
    }

    private List<List<Card>> selectNFromList(int n, List<Card> cards) {
        final List<List<Card>> combinations = Lists.newArrayList();
        if (n == 0) {
            combinations.add(Lists.newArrayList());
        } else if (n == cards.size()) {
            combinations.add(cards);
        } else {

            final Card firstCard = cards.get(0);

            // Including first card
            final List<List<Card>> combinationsOfRest = selectNFromList(n - 1, cards.subList(1, cards.size()));
            for (List<Card> combination : combinationsOfRest) {
                final List<Card> withFirstCard = Lists.newArrayList(firstCard);
                withFirstCard.addAll(combination);
                combinations.add(withFirstCard);
            }

            // Not including first card
            for (List<Card> combination : selectNFromList(n, cards.subList(1, cards.size()))) {
                combinations.add(combination);
            }

        }
        return combinations;
    }

    private boolean isSumOfPointsFifteen(List<Card> cards) {
        final int total = cards.stream()
                .mapToInt(x -> x.getRank().pointsValue())
                .sum();

        return total == 15;
    }

    private boolean isConsecutive(List<Card> cards) {
        final Set<Rank> uniqueRanks = cards.stream()
                .map(Card::getRank)
                .collect(toSet());

        final List<Rank> sortedRanks = Lists.newArrayList(uniqueRanks);
        sortedRanks.sort(Comparator.comparing(Rank::asInt));

        return sortedRanks.size() == cards.size() &&
                sortedRanks.get(0).asInt() + cards.size() - 1 == sortedRanks.get(cards.size() - 1).asInt();
    }
}
