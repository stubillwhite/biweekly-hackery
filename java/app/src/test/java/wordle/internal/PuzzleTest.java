package wordle.internal;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import testcommon.MockTestBase;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static wordle.internal.Feedback.*;
import static wordle.internal.PuzzleStatus.LOST;
import static wordle.internal.PuzzleStatus.WON;

public class PuzzleTest extends MockTestBase {

    private final String correctWord = "TONER";
    private final String incorrectWord = "VOTER";
    private final List<String> wordList = Lists.newArrayList(correctWord, incorrectWord);

    @Mock private Player mockPlayer;

    @Test
    public void nextStateGivenIncorrectGuessThenProvidesFeedback() {
        // Given
        when(mockPlayer.guessWord(anyList())).thenReturn(incorrectWord);
        final Puzzle puzzle = new Puzzle(wordList, mockPlayer, correctWord);

        // When
        puzzle.nextState();

        // Then
        final List<GuessFeedback> actual = puzzle.getGuessFeedback();
        assertThat(actual.size()).isEqualTo(1);

        final List<LetterFeedback> expected = List.of(
                new LetterFeedback('V', INCORRECT_LETTER),
                new LetterFeedback('O', CORRECT),
                new LetterFeedback('T', INCORRECT_LOCATION),
                new LetterFeedback('E', CORRECT),
                new LetterFeedback('R', CORRECT)
        );

        assertThat(actual.get(0).getLetterFeedback())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void nextStateGivenTooManyIncorrectGuessesThenPuzzleIsLost() {
        // Given
        when(mockPlayer.guessWord(anyList())).thenReturn(incorrectWord);
        final Puzzle puzzle = new Puzzle(wordList, mockPlayer, correctWord);

        // When
        IntStream.rangeClosed(1, 6).forEach(x -> puzzle.nextState());

        // Then
        assertThat(puzzle.getStatus()).isEqualTo(LOST);
    }

    @Test
    public void nextStateGivenPuzzleWonThenNoChange() {
        // Given
        when(mockPlayer.guessWord(anyList())).thenReturn(correctWord);
        final Puzzle puzzle = new Puzzle(wordList, mockPlayer, correctWord);

        // When
        IntStream.rangeClosed(1, 2).forEach(x -> puzzle.nextState());

        // Then
        assertThat(puzzle.getStatus()).isEqualTo(WON);
    }

    @Test
    public void nextStateGivenCorrectGuessThenPuzzleIsWon() {
        // Given
        when(mockPlayer.guessWord(anyList())).thenReturn(correctWord);
        final Puzzle puzzle = new Puzzle(wordList, mockPlayer, correctWord);

        // When
        puzzle.nextState();

        // Then
        assertThat(puzzle.getStatus()).isEqualTo(WON);
    }

    @Test
    public void nextStateGivenPuzzleLostThenNoChange() {
        // Given
        when(mockPlayer.guessWord(anyList())).thenReturn(incorrectWord);
        final Puzzle puzzle = new Puzzle(wordList, mockPlayer, correctWord);

        // When
        IntStream.rangeClosed(1, 7).forEach(x -> puzzle.nextState());

        // Then
        assertThat(puzzle.getStatus()).isEqualTo(LOST);
    }
}
