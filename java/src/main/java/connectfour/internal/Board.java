package connectfour.internal;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

public class Board {

    private final int width;
    private final int height;
    private final Map<Location, Color> boardContents = Maps.newHashMap();

    private Optional<Color> winner;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        winner = Optional.empty();
    }

    public Optional<Color> checkForWinningMove() {
        return winner;
    }

    public boolean dropToken(int column, Color color) {
        if (isValidMove(column)) {
            return false;
        } else {
            final Location location = new Location(column, lowestEmptyRowInColumn(column));

            boardContents.put(location, color);
            winner = checkForWinningMove(location, color);

            return true;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Location, Color> getBoardContents() {
        return boardContents;
    }

    public boolean isValidMove(int column) {
        return 0 <= column && column < width && lowestEmptyRowInColumn(column) < height;
    }

    private int lowestEmptyRowInColumn(int column) {
        for (int y = 0; y < height; y++) {
            if (!boardContents.containsKey(new Location(column, y))) {
                return y;
            }
        }
        return height;
    }

    private Optional<Color> checkForWinningMove(Location location, Color color) {
        if (hasLineOfFour(location)) {
            return Optional.of(color);
        }
        else {
            return Optional.empty();
        }
    }

    private boolean hasLineOfFour(Location location) {
        return hasLine(location, new Location(1, 1))        // Up-right
                || hasLine(location, new Location(1, 0))    // Right
                || hasLine(location, new Location(1, -1))   // Down-right
                || hasLine(location, new Location(0, -1))   // Down
                || hasLine(location, new Location(-1, -1))  // Down-left
                || hasLine(location, new Location(-1, 0))   // Left
                || hasLine(location, new Location(-1, 1));  // Up-left
    }

    private boolean hasLine(Location location, Location delta) {
        final Color color = boardContents.get(location);

        Location currLocation = location;
        for (int i = 0; i < 3; i++) {
            currLocation = currLocation.add(delta);
            final Color contents = boardContents.get(currLocation);
            if (contents == null || !contents.equals(color)) {
                return false;
            }
        }

        return true;
    }
}
