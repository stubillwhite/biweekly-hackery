package connectfour.internal;

import java.util.Scanner;

public class HumanPlayer implements Player {

    private final Scanner scanner = new Scanner(System.in);
    private final Color color;

    public HumanPlayer(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int chooseColumn(Board board) {
        return scanner.nextInt();
    }
}
