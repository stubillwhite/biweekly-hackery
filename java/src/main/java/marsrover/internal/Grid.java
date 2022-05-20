package marsrover.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Grid {

    private final int width;
    private final int height;

    private final List<Rover> rovers = Lists.newArrayList();

    public Grid(Location topRightLocation) {
        this.width = topRightLocation.getX() + 1;
        this.height = topRightLocation.getY() + 1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addRover(Rover rover) {
        rovers.add(rover);
    }

    public List<Rover> getRovers() {
        return rovers;
    }
}
