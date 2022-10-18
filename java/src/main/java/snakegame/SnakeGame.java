package snakegame;

import snakegame.internal.Direction;
import snakegame.internal.Location;
import snakegame.internal.Snake;
import snakegame.internal.TextUI;

import java.util.Random;

public class SnakeGame {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    private final TextUI textUI;
    private final Snake snake;
    private Location foodLocation;

    public SnakeGame(TextUI textUI,
                     Snake snake) {
        this.textUI = textUI;
        this.snake = snake;
        this.foodLocation = randomLocation();
    }

    public void tick() {
        snake.update();

        if (snake.getBody().contains(foodLocation)) {
            foodLocation = randomLocation();
            snake.growBy(3);
        }

        final StringBuilder builder = new StringBuilder();
        for (int y = HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x <= WIDTH - 1; x++) {
                final Location location = new Location(x, y);

                if (snake.getBody().contains(location)) {
                    builder.append("@");
                }
                else if (location.equals(foodLocation)) {
                    builder.append("*");
                }
                else {
                    builder.append(" ");
                }
            }
            builder.append("\n");
        }
        textUI.setContent(builder.toString());
    }

    private Location randomLocation() {
        final Random random = new Random();
        return new Location(random.nextInt(WIDTH), random.nextInt(HEIGHT));
    }

    public static void main(String[] args) throws Exception {
        final Snake snake = new Snake(new Location(5, 5), Direction.RIGHT, 3);
        final TextUI.Controller controller = snake::setDesiredDirection;
        final TextUI textUI = TextUI.createTextUI(WIDTH, HEIGHT, controller);
        final SnakeGame game = new SnakeGame(textUI, snake);
        while (true) {
            game.tick();
            Thread.sleep(100);
        }

    }
}
