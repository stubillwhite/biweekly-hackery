package wordle.internal;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HumanPlayerTest {

    @Test
    public void guessWordThenReturnsNextWordFromStdIn() {
        // Given
        setStdIn("word-1" + System.lineSeparator() + "word-2" + System.lineSeparator());
        final HumanPlayer humanPlayer = new HumanPlayer();

        // When
        final String actual = humanPlayer.guessWord(List.of());

        // Then
        assertThat(actual).isEqualTo("word-1");
    }

    private void setStdIn(String content) {
        System.setIn(new ByteArrayInputStream(content.getBytes()));
    }
}
