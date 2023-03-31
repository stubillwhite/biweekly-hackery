package marsrover.internal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputParser {

    public Grid parseGrid(String dimensions) {
        final Pattern pattern = Pattern.compile("(\\d+) (\\d+)");

        final Matcher matcher = pattern.matcher(dimensions);
        matcher.find();

        return new Grid(new Location(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))));
    }

    public Rover parseRover(String locationAndBearing) {
        final Pattern pattern = Pattern.compile("(\\d+) (\\d+) ([NESW])");

        final Matcher matcher = pattern.matcher(locationAndBearing);
        matcher.find();

        final int x = Integer.parseInt(matcher.group(1));
        final int y = Integer.parseInt(matcher.group(2));
        final Bearing bearing = Bearing.fromCharacterCode(matcher.group(3));

        return new Rover(new Location(x, y), bearing);
    }

    public List<Instruction> parseInstructions(String instructionCodes) {
        return instructionCodes.chars()
                .mapToObj(x -> (char) x)
                .map(x -> Instruction.fromCharacterCode(x.toString()))
                .collect(Collectors.toList());
    }
}
