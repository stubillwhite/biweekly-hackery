package wordle.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static wordle.internal.Feedback.*;

public class TextDisplay implements Display {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    private static final String ALPHABET =
                    "Q W E R T Y U I O P" + "\n" +
                    " A S D F G H J K L " + "\n" +
                    "   Z X C V B N M   ";

    @Override
    public void display(Puzzle puzzle) {
        System.out.println();
        final List<GuessFeedback> guessFeedback = puzzle.getGuessFeedback();

        System.out.println(renderGuessFeedback(guessFeedback));
        System.out.println();
        System.out.println(renderAlphabet(guessFeedback));
        System.out.println();

        switch (puzzle.getStatus()) {
            case WON:
                System.out.println("You win!");
                break;

            case LOST:
                System.out.printf("Sorry! The word was %s. Better luck next time!\n", puzzle.getWord());
                break;

            default:
                break;
        }
    }

    private String renderGuessFeedback(List<GuessFeedback> feedback) {
        final StringBuilder builder = new StringBuilder();
        for (GuessFeedback guessFeedback : feedback) {
            for (LetterFeedback letterFeedback : guessFeedback.getLetterFeedback()) {
                builder.append(renderLetter(letterFeedback.getLetter(), letterFeedback.getFeedback()));
                builder.append(" ");
            }
            builder.append("\n\n");
        }
        return builder.toString();
    }

    private String renderAlphabet(List<GuessFeedback> guessFeedback) {

        final Map<Character, Set<Feedback>> feedbackByLetter = guessFeedback.stream()
                .flatMap(x -> x.getLetterFeedback().stream())
                .collect(Collectors.groupingBy(
                        LetterFeedback::getLetter,
                        Collectors.mapping(LetterFeedback::getFeedback, Collectors.toSet())));

        String alphabet = ALPHABET;
        for (Character letter : feedbackByLetter.keySet()) {
            final Feedback bestFeedback = bestLetterStatus(feedbackByLetter.get(letter));
            alphabet = alphabet.replace(letter.toString(), renderLetter(letter, bestFeedback));
        }

        return alphabet;
    }

    private Feedback bestLetterStatus(Set<Feedback> letterStatuses) {
        if (letterStatuses.contains(CORRECT)) {
            return CORRECT;
        }
        else if (letterStatuses.contains(INCORRECT_LOCATION)) {
            return INCORRECT_LOCATION;
        }
        else {
            return INCORRECT_LETTER;
        }
    }

    private String renderLetter(Character letter, Feedback feedback) {
        String backgroundColor = null;

        switch (feedback) {
            case CORRECT:
                backgroundColor = ANSI_GREEN_BACKGROUND;
                break;

            case INCORRECT_LOCATION:
                backgroundColor = ANSI_YELLOW_BACKGROUND;
                break;

            case INCORRECT_LETTER:
                backgroundColor = ANSI_RED_BACKGROUND;
                break;
        }

        return ANSI_BLACK + backgroundColor + letter.toString() + ANSI_RESET;
    }
}
