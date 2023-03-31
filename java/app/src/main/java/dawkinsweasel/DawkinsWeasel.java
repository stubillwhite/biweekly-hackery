package dawkinsweasel;

import dawkinsweasel.internal.Simulation;

public class DawkinsWeasel {

    public static void main(String[] args) {
        final Simulation simulation = new Simulation();
        simulation.run();
        System.out.println("Done");
    }
}
