package snakegame.internal.player;

import snakegame.internal.Direction;
import snakegame.internal.Location;
import snakegame.internal.Snake;
import snakegame.internal.TextUI.KeyPressListener;
import snakegame.internal.pathfinding.SnakePathfinder;

import java.util.List;

import static snakegame.internal.Direction.*;

public class ComputerPlayer implements Player, KeyPressListener {

    private final SnakePathfinder pathfinder = new SnakePathfinder();

    @Override
    public Direction getDesiredDirection(Snake snake, Location foodLocation) {
        final Location head = snake.getBody().get(0);

        final List<Location> path = pathfinder.findPath(snake, foodLocation);
        final Location nextNode = path.get(1);
        System.out.printf("Head %s, Food: %s, Next: %s, Direction %s\n",
                head, foodLocation, nextNode, snake.getCurrentDirection());

        if (nextNode.getX() < head.getX()) return LEFT;
        else if (nextNode.getX() > head.getX()) return RIGHT;
        else if (nextNode.getY() < head.getY()) return DOWN;
        else if (nextNode.getY() > head.getY()) return UP;

        return snake.getCurrentDirection();
    }

    @Override
    public void handleKeyPress(Direction direction) {
    }
}
