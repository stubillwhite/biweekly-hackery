package wordle;

import wordle.internal.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static wordle.internal.PuzzleStatus.IN_PROGRESS;

public class Wordle {

    private static final String FILENAME = "wordle/corncob-caps.txt";

    public static void main(String[] args) {
        final List<String> wordList = loadWordList(FILENAME);
        final Display display = new TextDisplay();
        final Player player = new HumanPlayer();

        final Puzzle puzzle = Puzzle.newPuzzle(wordList, player);

        while (puzzle.getStatus() == IN_PROGRESS) {
            display.display(puzzle);
            puzzle.nextState();
        }

        display.display(puzzle);
    }

    private static List<String> loadWordList(String filename) {
        final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().collect(Collectors.toList());
    }
}
