package liftkata;

import com.google.common.collect.Lists;
import liftkata.internal.Building;
import liftkata.internal.Elevator;
import liftkata.internal.Floor;

import java.util.List;

public class LiftKata {
    public static void main(String[] args) {
        final Elevator elevator = new Elevator();

        final List<Floor> floors = Lists.newArrayList(
                new Floor(),
                new Floor(),
                new Floor(),
                new Floor(),
                new Floor()
        );

        new Building(elevator, floors);

        System.out.println("Done");
    }
}
