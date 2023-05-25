package liftkata;

import com.google.common.collect.Lists;
import liftkata.internal.*;

import java.util.List;

public class LiftKata {
    public static void main(String[] args) {
        final Elevator elevator = new Elevator();

        final List<Floor> floors = Lists.newArrayList(
                new Floor(0),
                new Floor(1),
                new Floor(2),
                new Floor(3),
                new Floor(4)
        );

        final Passenger passengerA = new Passenger("A", 1);
        final Passenger passengerB = new Passenger("B", 2);
        final Passenger passengerC = new Passenger("C", 3);
        final Passenger passengerD = new Passenger("D", 4);

        floors.get(0).getPassengers().add(passengerA);
        floors.get(1).getPassengers().add(passengerB);
        floors.get(2).getPassengers().add(passengerC);

        elevator.embark(passengerD);

        final Building building = new Building(elevator, floors);

        final Display display = new Display();
        display.display(building);

        System.out.println("Done");
    }
}
