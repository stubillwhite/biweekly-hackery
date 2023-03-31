package connectfour.internal;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Display {

    private static final String TOKEN = "\u25cf";
    private static final String EMPTY = "\u25cb";
    private static final String COLOR_YELLOW = "\033[0;33m";
    private static final String COLOR_RED = "\033[0;31m";
    private static final String COLOR_NONE = "\033[0m";

    public void display(Board board) {
        final String boardAsString =
                IntStream.iterate(board.getHeight() - 1, y -> y - 1).limit(board.getHeight()).boxed().map(y ->
                        IntStream.range(0, board.getWidth()).boxed().map(x -> symbolForLocation(board.getBoardContents(), x, y)
                        ).collect(Collectors.joining(" ")) + "\n"
                ).collect(Collectors.joining());

        final String columnNumbersAsString =
                IntStream.range(0, board.getWidth())
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(" "));

        System.out.println();
        System.out.print(boardAsString);
        System.out.println(columnNumbersAsString);
    }

    private String symbolForLocation(Map<Location, Color> boardContents, Integer x, Integer y) {
        final Location location = new Location(x, y);
        if (boardContents.containsKey(location)) {
            final Color color = boardContents.get(location);
            return String.format("%s%s%s", asciiCodeForColor(color), TOKEN, COLOR_NONE);
        } else {
            return EMPTY;
        }
    }

    private String asciiCodeForColor(Color color) {
        switch (color) {
            case RED:
                return COLOR_RED;

            case YELLOW:
                return COLOR_YELLOW;

            default:
                throw new EnumConstantNotPresentException(Color.class, "color");
        }
    }
}
