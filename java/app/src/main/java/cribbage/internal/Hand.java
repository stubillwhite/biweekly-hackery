package cribbage.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Hand {
    private final List<Card> cards;
    private final Card faceUpCard;

    public Hand(List<Card> cards, Card faceUpCard) {
        this.cards = cards;
        this.faceUpCard = faceUpCard;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card getFaceUpCard() {
        return faceUpCard;
    }

    public List<Card> getAllCards() {
        final List<Card> allCards = Lists.newArrayList();
        allCards.addAll(cards);
        allCards.add(faceUpCard);
        return allCards;
    }

    @Override
    public String toString() {
        return DisplayUtils.toString(this);
    }
}
