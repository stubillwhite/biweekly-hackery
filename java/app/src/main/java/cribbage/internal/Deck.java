package cribbage.internal;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        final List<Card> allCards = Lists.newArrayList();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                allCards.add(new Card(rank, suit));
            }
        }

        cards = allCards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.remove(0);
    }
}
