package snakegame.internal.player;

import snakegame.internal.Direction;
import snakegame.internal.Snake;
import snakegame.internal.TextUI.UserInputHandler;

public abstract class Player implements UserInputHandler {

    @Override
    public void handleDirection(Snake snake, Direction desiredDirection) {
    }

    @Override
    public void handleUpdate(Snake snake) {
    }
}
