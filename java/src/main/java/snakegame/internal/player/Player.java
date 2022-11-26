package snakegame.internal.player;

import snakegame.internal.Direction;
import snakegame.internal.Location;
import snakegame.internal.Snake;

public interface Player {
    Direction getDesiredDirection(Snake snake, Location foodLocation);
}
