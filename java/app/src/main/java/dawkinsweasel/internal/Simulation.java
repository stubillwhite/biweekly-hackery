package dawkinsweasel.internal;

import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Simulation {

    /* This is a simple solution using old for-loops. When we look at streams we can revisit this to Java's stream API
     * and map functions. This also isn't very testable in the current form. Once we've done a few more of these, we
     * can return and see how we might do this in a more testable way.
     */

    private static final String ALL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
    private static final String TARGET_STRING = "METHINKS IT IS LIKE A WEASEL";
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_PROBABILITY = 0.05;

    private final Random random;

    public Simulation() {
        this.random = new Random();
    }

    public void run() {
        List<String> population = reproduce(randomIndividual());

        while (!TARGET_STRING.equals(findFittest(population))) {
            final String fittest = findFittest(population);
            displayFittest(population);

            population = mutate(reproduce(fittest));
        }

        displayFittest(population);
    }

    private Character randomCharacter() {
        final int index = random.nextInt(ALL_CHARACTERS.length());
        return ALL_CHARACTERS.toCharArray()[index];
    }

    private String randomIndividual() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < TARGET_STRING.length(); i++) {
            stringBuilder.append(randomCharacter());
        }
        return stringBuilder.toString();
    }

    private List<String> reproduce(String individual) {
        final List<String> population = Lists.newArrayList();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new String(individual));
        }
        return population;
    }

    private String mutate(String individual) {
        final char[] chars = individual.toCharArray();
        for (int i = 0; i < individual.length(); i++) {
            if (random.nextDouble() < MUTATION_PROBABILITY) {
                chars[i] = randomCharacter();
            }
        }
        return new String(chars);
    }

    private List<String> mutate(List<String> population) {
        for (int i = 0; i < population.size(); i++) {
            population.set(i, mutate(population.get(i)));
        }
        return population;
    }

    private int fitnessScore(String individual) {
        int score = 0;
        for (int i = 0; i < TARGET_STRING.length(); i++) {
            if (individual.charAt(i) == TARGET_STRING.charAt(i)) {
                score += 1;
            }
        }
        return score;
    }

    private String findFittest(List<String> population) {
        final Comparator<String> fitnessComparator = new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return Integer.compare(fitnessScore(a), fitnessScore(b));
            }
        };

        population.sort(fitnessComparator.reversed());
        return population.get(0);
    }

    private void displayFittest(List<String> population) {
        final String fittest = findFittest(population);
        System.out.printf("Fittest: %s (%d)\n", fittest, fitnessScore(fittest));
    }
}
