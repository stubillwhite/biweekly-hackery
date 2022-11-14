package connectfour.internal;

public interface Player {

    Color getColor();

    int chooseColumn(Board board);
}
