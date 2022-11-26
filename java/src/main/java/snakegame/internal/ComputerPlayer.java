package snakegame.internal;

import com.google.common.collect.Lists;
import snakegame.internal.TextUI.KeyPressListener;

import java.util.Random;

import static snakegame.internal.Direction.*;

public class ComputerPlayer implements Player, KeyPressListener {

    @Override
    public Direction getDesiredDirection(Snake snake, Location foodLocation) {
        final int idx = new Random().nextInt(4);
        return Lists.newArrayList(LEFT, RIGHT, UP, DOWN).get(idx);
    }

    @Override
    public void handleKeyPress(Direction direction) {
    }
}
