package wordle.internal;

import java.util.List;

public interface Player {
    String guessWord(List<GuessFeedback> guessFeedback);
}
