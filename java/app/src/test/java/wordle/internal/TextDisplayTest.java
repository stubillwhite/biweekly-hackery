package wordle.internal;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import testcommon.MockTestBase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static wordle.internal.Feedback.*;
import static wordle.internal.PuzzleStatus.*;

public class TextDisplayTest extends MockTestBase {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    private static final PrintStream STDOUT = System.out;

    private final String correctWord = "TONER";
    private final String incorrectWord = "VOTER";
    private final List<String> wordList = Lists.newArrayList(correctWord, incorrectWord);

    @Mock private Puzzle stubPuzzle;

    @Test
    public void displayGivenInProgressThenRendersFeedback() {
        // Given
        final GuessFeedback incorrectGuessFeedback = new GuessFeedback(List.of(
                new LetterFeedback('V', INCORRECT_LETTER),
                new LetterFeedback('O', CORRECT),
                new LetterFeedback('T', INCORRECT_LOCATION),
                new LetterFeedback('E', CORRECT),
                new LetterFeedback('R', CORRECT)
        ));
        final List<GuessFeedback> guessFeedback = List.of(incorrectGuessFeedback);
        when(stubPuzzle.getGuessFeedback()).thenReturn(guessFeedback);
        when(stubPuzzle.getStatus()).thenReturn(IN_PROGRESS);

        // When
        final String actual = displayToString(stubPuzzle);

        // Then
        final String expected = String.join("\n", List.of(
                "",
                String.format("%s %s %s %s %s ", incorrectLetter('V'), correct('O'), incorrectLocation('T'), correct('E'), correct('R')),
                "",
                "",
                "",
                String.format("Q W %s %s %s Y U I %s P", correct('E'), correct('R'), incorrectLocation('T'), correct('O')),
                " A S D F G H J K L ",
                String.format("   Z X C %s B N M   ", incorrectLetter('V')),
                "",
                ""
                ));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void displayGivenWonThenIncludesWinningMessage() {
        when(stubPuzzle.getGuessFeedback()).thenReturn(List.of());
        when(stubPuzzle.getStatus()).thenReturn(WON);

        // When
        final String actual = displayToString(stubPuzzle);

        // Then
        final List<String> lines = List.of(actual.split("\\n"));
        final String actualMessage = lines.get(lines.size()-1);

        assertThat(actualMessage).isEqualTo("You win!");
    }

    @Test
    public void displayGivenWonThenIncludesLosingMessage() {
        when(stubPuzzle.getGuessFeedback()).thenReturn(List.of());
        when(stubPuzzle.getWord()).thenReturn(correctWord);
        when(stubPuzzle.getStatus()).thenReturn(LOST);

        // When
        final String actual = displayToString(stubPuzzle);

        // Then
        final List<String> lines = List.of(actual.split("\\n"));
        final String actualMessage = lines.get(lines.size()-1);

        assertThat(actualMessage).isEqualTo("Sorry! The word was " + correctWord + ". Better luck next time!");
    }

    private String displayToString(Puzzle puzzle) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream redirectedStdOut = new PrintStream(outputStream);
        System.setOut(redirectedStdOut);
        new TextDisplay().display(puzzle);
        System.setOut(STDOUT);

        return outputStream.toString();
    }

    private String incorrectLetter(Character letter) {
        return ANSI_BLACK + ANSI_RED_BACKGROUND + letter.toString() + ANSI_RESET;
    }

    private String incorrectLocation(Character letter) {
        return ANSI_BLACK + ANSI_YELLOW_BACKGROUND + letter.toString() + ANSI_RESET;
    }

    private String correct(Character letter) {
        return ANSI_BLACK + ANSI_GREEN_BACKGROUND + letter.toString() + ANSI_RESET;
    }
}
