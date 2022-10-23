package snakegame.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static snakegame.internal.Direction.UP;
import static snakegame.internal.Direction.RIGHT;
import static snakegame.internal.Direction.DOWN;
import static snakegame.internal.Direction.LEFT;

public class Snake {

    private static final Map<Direction, Set<Direction>> ALLOWED_DIRECTION_CHANGES =
            ImmutableMap.of(
                    UP, Sets.newHashSet(LEFT, RIGHT),
                    RIGHT, Sets.newHashSet(UP, DOWN),
                    DOWN, Sets.newHashSet(RIGHT, LEFT),
                    LEFT, Sets.newHashSet(DOWN, UP));

    private List<Location> body;
    private Direction currentDirection;
    private Direction desiredDirection;
    private int length;

    public Snake(Location head, Direction direction, int length) {
        this.body = Lists.newLinkedList();
        this.body.add(head);
        this.currentDirection = direction;
        this.desiredDirection = direction;
        this.length = length;
    }

    public void update() {
        currentDirection = desiredDirection;
        final Location newHead = getLocationAhead();
        body.add(0, newHead);
        body = body.subList(0, Math.min(length, body.size()));
    }

    public List<Location> getBody() {
        return body;
    }

    public void growBy(int additionalLength) {
        length += additionalLength;
    }

    public void setDesiredDirection(Direction direction) {
        if (ALLOWED_DIRECTION_CHANGES.get(currentDirection).contains(direction)) {
            desiredDirection = direction;
        }
    }

    private Location getLocationAhead() {
        final Location head = body.get(0);
        switch (currentDirection) {
            case UP:
                return new Location(head.getX(), head.getY() + 1);

            case RIGHT:
                return new Location(head.getX() + 1, head.getY());

            case DOWN:
                return new Location(head.getX(), head.getY() - 1);

            case LEFT:
                return new Location(head.getX() - 1, head.getY());

            default:
                throw new IllegalStateException("Unknown direction " + currentDirection);
        }
    }

    @Override
    public String toString() {
        return body.stream()
                .map(Location::toString)
                .collect(Collectors.joining(", "));
    }
}