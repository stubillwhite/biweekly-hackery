package snakegame.internal;

import com.google.common.collect.Lists;
import snakegame.internal.TextUI.KeyPressListener;
import snakegame.internal.pathfinding.LocationPathfinder;
import snakegame.internal.pathfinding.Pathfinder;

import java.util.List;
import java.util.Random;

import static snakegame.internal.Direction.*;

public class ComputerPlayer implements Player, KeyPressListener {

    private final LocationPathfinder pathfinder = new LocationPathfinder();

    @Override
    public Direction getDesiredDirection(Snake snake, Location foodLocation) {
        final Location head = snake.getBody().get(0);
        System.out.printf("Head: %s\n", head);
        System.out.printf("Food: %s\n", foodLocation);

        final List<Location> path = pathfinder.findPath(head, foodLocation);
        System.out.printf("Next: %s\n", path.get(0));

        if (foodLocation.getX() < head.getX()) return LEFT;
        else if (foodLocation.getX() > head.getX()) return RIGHT;
        else if (foodLocation.getY() < head.getY()) return DOWN;
        else if (foodLocation.getY() > head.getY()) return UP;
        else return snake.getCurrentDirection();
    }

    @Override
    public void handleKeyPress(Direction direction) {
    }
}
