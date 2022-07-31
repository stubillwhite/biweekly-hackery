package elevatorpitch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Simulation {

    private final Set<Person> people;

    public Simulation(Person... persons, Elevator elevator) {
        people = new HashSet<Person>();
        people.addAll(Arrays.asList(persons));
    }

    public Set<Person> getPeople() {
        return people;
    }
}
