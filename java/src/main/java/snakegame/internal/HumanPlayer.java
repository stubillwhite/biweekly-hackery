package snakegame.internal;

import snakegame.internal.TextUI.KeyPressListener;

public class HumanPlayer implements Player, KeyPressListener {

    private Direction desiredDirection;

    @Override
    public Direction getDesiredDirection(Snake snake, Location foodLocation) {
        return desiredDirection;
    }

    @Override
    public void handleKeyPress(Direction direction) {
        desiredDirection = direction;
    }
}
