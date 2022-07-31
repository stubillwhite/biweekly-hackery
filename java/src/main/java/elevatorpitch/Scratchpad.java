package elevatorpitch;

public class Scratchpad {

    // Elevators with people moving between floors
    // One elevator per building

    public static void main(String[] args) {

        final Person person1 = new Person(1, 2);
        final Person person2 = new Person(2, 1);

        final Simulation building = new Simulation(person1, person2);


        System.out.println("Done");
    }
}
