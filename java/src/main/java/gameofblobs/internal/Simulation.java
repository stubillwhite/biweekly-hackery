package gameofblobs.internal;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;

import java.util.List;
import java.util.stream.Stream;

public final class Simulation {

    private final World world;
    private final Display display;

    public Simulation(World world,
                      Display display) {

        this.world = world;
        this.display = display;
    }

    public void run() {
        final PeekingIterator<World> states =
                Iterators.peekingIterator(Stream.iterate(world, World::nextState).iterator());

        World currentState = states.next();
        while (states.hasNext() && !states.peek().equals(currentState)) {
            display.display(currentState);
            currentState = states.next();
            pauseBriefly();
        }
        display.display(currentState);
    }

    private static void pauseBriefly() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
