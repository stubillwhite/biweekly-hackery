package snakegame.internal;

public interface Player {
    Direction getDesiredDirection(Snake snake, Location foodLocation);
}
