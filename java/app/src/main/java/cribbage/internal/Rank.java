package cribbage.internal;

import com.google.common.collect.Sets;

import java.util.Set;

public enum Rank {
    Ace,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King;

    private static final Set<Rank> FACE_CARDS = Sets.newHashSet(Jack, Queen, King);

    public int asInt() {
        return this.ordinal() + 1;
    }

    public int pointsValue() {
        if (FACE_CARDS.contains(this)) {
            return 10;
        }
        else {
            return this.asInt();
        }
    }
}
