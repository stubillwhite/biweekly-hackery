package cribbage;

import com.google.common.collect.Lists;
import cribbage.internal.Card;
import cribbage.internal.Deck;
import cribbage.internal.Hand;
import cribbage.internal.HandScorer;

import java.util.List;

public class Cribbage {

    public static void main(String[] args) {
        final Deck deck = new Deck();
        deck.shuffle();

        final List<Card> playerCards = Lists.newArrayList();
        playerCards.add(deck.deal());
        playerCards.add(deck.deal());
        playerCards.add(deck.deal());
        playerCards.add(deck.deal());

        final Card faceUpCard = deck.deal();

        final Hand hand = new Hand(playerCards, faceUpCard);

        final HandScorer scorer = new HandScorer();

        System.out.printf("Hand: %s, score: %d", hand, scorer.scoreHand(hand));
    }
}
