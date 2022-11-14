package connectfour;

import com.google.common.collect.Queues;
import connectfour.internal.*;

import java.util.Deque;

public class ConnectFour {
    public static void main(String[] args) {
        final Board board = new Board(10, 10);
        final Display display = new Display();

        final HumanPlayer red = new HumanPlayer(Color.RED);
        final HumanPlayer yellow = new HumanPlayer(Color.YELLOW);

        final Deque<Player> players = Queues.newArrayDeque();
        players.add(red);
        players.add(yellow);

        while (board.checkForWinningMove().isEmpty()) {
            display.display(board);

            final Player player = players.removeFirst();

            int column = chooseColumn(board, player);
            board.dropToken(column, player.getColor());

            players.addLast(player);
        }

        display.display(board);

        final Color winningColor = board.checkForWinningMove().get();
        System.out.printf("Congratulations! %s wins!\n", winningColor);
    }

    private static int chooseColumn(Board board, Player player) {
        int column;
        do {
            column = player.chooseColumn(board);
        } while (!board.isValidMove(column));
        return column;
    }

}
