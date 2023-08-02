package cribbage.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DisplayUtils {

    private static final Map<Character, Rank> CHAR_TO_RANK;

    static {
        final Map<Character, Rank> map = Maps.newHashMap();
        final Map<Character, Rank> twoToNine = IntStream.range(2, 10)
                .boxed()
                .collect(Collectors.toMap(
                        k -> Character.forDigit(k, 10),
                        v -> Rank.values()[v - 1]));
        map.putAll(twoToNine);
        map.put('T', Rank.Ten);
        map.put('J', Rank.Jack);
        map.put('Q', Rank.Queen);
        map.put('K', Rank.King);
        map.put('A', Rank.Ace);
        CHAR_TO_RANK = map;
    }

    private static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private static final Map<Character, Suit> CHAR_TO_SUIT;

    static {
        final Map<Character, Suit> map = Maps.newHashMap();
        map.put('C', Suit.Clubs);
        map.put('D', Suit.Diamonds);
        map.put('H', Suit.Hearts);
        map.put('S', Suit.Spades);
        CHAR_TO_SUIT = map;
    }

    private static final Map<Suit, Character> SUIT_TO_CHAR = invertMap(CHAR_TO_SUIT);
    private static final Map<Rank, Character> RANK_TO_CHAR = invertMap(CHAR_TO_RANK);

    public static Hand toHand(String str) {
        final List<Card> cards = Lists.newArrayList();
        for (String cardStr : str.split(",")) {
            cards.add(stringToCard(cardStr));
        }
        return new Hand(cards.subList(0, 4), cards.get(4));
    }

    public static String toString(Hand hand) {
        return hand.getAllCards().stream()
                .map(DisplayUtils::cardToString)
                .collect(Collectors.joining(","));

    }

    public static Card stringToCard(String cardStr) {
        final Rank rank = CHAR_TO_RANK.get(cardStr.charAt(0));
        final Suit suit = CHAR_TO_SUIT.get(cardStr.charAt(1));
        return new Card(rank, suit);
    }

    public static String cardToString(Card card) {
        return RANK_TO_CHAR.get(card.getRank()).toString() + SUIT_TO_CHAR.get(card.getSuit()).toString();
    }
}
