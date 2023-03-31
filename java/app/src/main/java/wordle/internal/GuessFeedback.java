package wordle.internal;

import java.util.List;

public class GuessFeedback {
    private final List<LetterFeedback> letterFeedback;

    public GuessFeedback(List<LetterFeedback> letterFeedback) {
        this.letterFeedback = letterFeedback;
    }

    public List<LetterFeedback> getLetterFeedback() {
        return letterFeedback;
    }
}
