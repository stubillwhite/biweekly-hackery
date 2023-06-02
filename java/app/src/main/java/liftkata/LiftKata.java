package liftkata;

import liftkata.internal.FIFODestinationController;
import liftkata.internal.Simulation;

public class LiftKata {
    public static void main(String[] args) {
        final Simulation simulation =
                Simulation.builder()
                        .withElevator(new FIFODestinationController())
                        .withFloors(5)
                        .withPassenger("A", 4, 1)
                        .withPassenger("B", 2, 0)
                        .withPassenger("C", 3, 2)
                        .withDisplay()
                        .build();

        simulation.run();

        System.out.println("Done");
    }
}
