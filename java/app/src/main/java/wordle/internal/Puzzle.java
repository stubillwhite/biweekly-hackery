package wordle.internal;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static wordle.internal.Feedback.*;
import static wordle.internal.PuzzleStatus.*;

public class Puzzle {

    private final List<String> wordList;
    private final Player player;
    private final String word;
    private final List<GuessFeedback> guessFeedback;

    private PuzzleStatus puzzleStatus;

    public static Puzzle newPuzzle(List<String> wordList, Player player) {
        final List<String> validWords = wordList.stream()
                .filter(s -> s.length() == 5)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        Collections.shuffle(validWords);
        final String word = validWords.get(0);

        return new Puzzle(wordList, player, word);
    }

    public Puzzle(List<String> wordList,
                  Player player,
                  String word) {
        this.wordList = wordList;
        this.player = player;
        this.word = word;
        this.puzzleStatus = IN_PROGRESS;
        this.guessFeedback = Lists.newArrayList();
    }

    public void nextState() {
        switch (puzzleStatus) {
            case IN_PROGRESS:
                final String guess = nextValidGuessFromPlayer();

                guessFeedback.add(getFeedback(word, guess));

                if (word.equals(guess)) {
                    puzzleStatus = WON;
                } else if (guessFeedback.size() == 6) {
                    puzzleStatus = LOST;
                } else {
                    puzzleStatus = IN_PROGRESS;
                }
                break;

            case WON:
            case LOST:
                break;
        }
    }

    private String nextValidGuessFromPlayer() {
        String guess;

        do {
            guess = player.guessWord(guessFeedback).toUpperCase();
        } while (!wordList.contains(guess));

        return guess;
    }

    private GuessFeedback getFeedback(String word, String guess) {
        final List<LetterFeedback> letterFeedback = IntStream.range(0, word.length())
                .mapToObj(idx -> {
                    final Character wordLetter = word.charAt(idx);
                    final Character guessLetter = guess.charAt(idx);

                    Feedback feedback;
                    if (wordLetter == guessLetter) {
                        feedback = CORRECT;
                    } else if (word.contains(guessLetter.toString())) {
                        feedback = INCORRECT_LOCATION;
                    } else {
                        feedback = INCORRECT_LETTER;
                    }

                    return new LetterFeedback(guessLetter, feedback);
                })
                .collect(Collectors.toList());

        return new GuessFeedback(letterFeedback);
    }

    public PuzzleStatus getStatus() {
        return puzzleStatus;
    }

    public List<GuessFeedback> getGuessFeedback() {
        return guessFeedback;
    }

    public String getWord() {
        return word;
    }
}
