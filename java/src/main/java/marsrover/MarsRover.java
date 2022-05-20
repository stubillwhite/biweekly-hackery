package marsrover;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import marsrover.internal.*;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class MarsRover {

    public static void main(String[] args) throws Exception {
        final String program = createTestProgram();

        final InputParser inputParser = new InputParser();

        final Deque<String> lines = Queues.newArrayDeque(Arrays.asList(program.split("\n")));

        final Grid grid = inputParser.parseGrid(lines.pop());
        final Display display = new Display(grid);

        while (!lines.isEmpty()) {
            final Rover rover = inputParser.parseRover(lines.pop());
            grid.addRover(rover);
            displayGrid(display);

            final List<Instruction> instructions = inputParser.parseInstructions(lines.pop());
            for (Instruction instruction : instructions) {
                rover.executeInstruction(instruction);
                displayGrid(display);
            }
        }

        displayFinalState(display);
        System.out.println("Done");
    }

    private static void displayGrid(Display display) throws InterruptedException {
        System.out.println(display.renderGrid());
        System.out.printf("\033[%dA", display.getGrid().getHeight()+2);
        System.out.println();
        Thread.sleep(500);
    }

    private static void displayFinalState(Display display) {
        System.out.println(display.renderGrid());

        for (Rover rover : display.getGrid().getRovers()) {
            final Location location = rover.getLocation();
            System.out.printf("%d %d %s\n", location.getX(), location.getY(), rover.getBearing().getCharacterCode());
        }
    }

    private static String createTestProgram() {
        final List<String> lines = Lists.newArrayList("5 5", "1 2 N", "LMLMLMLMM", "3 3 E", "MMRMMRMRRM");
        return String.join("\n", lines);
    }
}
