package snakegame;

import snakegame.internal.*;

import java.util.Random;

public class SnakeGame {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    private final TextUI textUI;
    private final Player player;
    private final Snake snake;

    private Location foodLocation;

    public SnakeGame(TextUI textUI,
                     Player player,
                     Snake snake) {
        this.textUI = textUI;
        this.player = player;
        this.snake = snake;
        this.foodLocation = randomLocation();
    }

    public void tick() {
        snake.setDesiredDirection(player.getDesiredDirection(snake, foodLocation));

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
                } else if (location.equals(foodLocation)) {
                    builder.append("*");
                } else {
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

//        final HumanPlayer player = new HumanPlayer();
        final ComputerPlayer player = new ComputerPlayer();

        final TextUI textUI = TextUI.createTextUI(WIDTH, HEIGHT, player);
        final SnakeGame game = new SnakeGame(textUI, player, snake);
        while (true) {
            game.tick();
            Thread.sleep(100);
        }

    }
}
