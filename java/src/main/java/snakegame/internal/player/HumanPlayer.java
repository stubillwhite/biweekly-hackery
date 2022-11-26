package snakegame.internal.player;

import snakegame.internal.Direction;
import snakegame.internal.Snake;

public class HumanPlayer extends Player {

    @Override
    public void handleDirection(Snake snake, Direction desiredDirection) {
        snake.setDesiredDirection(desiredDirection);
    }
}
