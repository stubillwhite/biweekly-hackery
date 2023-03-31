package wordle.internal;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer implements Player {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String guessWord(List<GuessFeedback> guessFeedback) {
        return scanner.nextLine().trim();
    }
}
