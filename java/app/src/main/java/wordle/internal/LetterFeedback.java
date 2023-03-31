package wordle.internal;

public class LetterFeedback {
    private final Character letter;
    private final Feedback feedback;

    public LetterFeedback(Character letter, Feedback feedback) {
        this.letter = letter;
        this.feedback = feedback;
    }

    public Character getLetter() {
        return letter;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}
