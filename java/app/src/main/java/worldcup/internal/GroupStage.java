package worldcup.internal;

import java.util.*;
import java.util.stream.Collectors;

public class GroupStage implements Stage {

    private final List<Team> teams;

    private Optional<Team> winner = Optional.empty();
    private Optional<Team> runnerUp = Optional.empty();

    public GroupStage(Team... teams) {
        this.teams = Arrays.asList(teams);
    }

    @Override
    public List<Optional<Team>> getTeams() {
        return teams.stream()
                .map(Optional::of)
                .collect(Collectors.toList());
    }

    @Override
    public void predictOutcome(ELORatingBasedPredictor predictor) {
        final Map<Team, Integer> teamPoints = teams.stream().collect(Collectors.toMap(x -> x, x -> 0));

        final List<List<Team>> pairings = Combinatorics.selectionsOfNFromList(2, teams);

        pairings.forEach(teams -> {
            final Team a = teams.get(0);
            final Team b = teams.get(1);

            final Team winner = predictor.predictWinner(a, b);

            final boolean isWinForA = a == winner;

            a.setRating(predictor.newRating(a, b, isWinForA));
            b.setRating(predictor.newRating(b, a, !isWinForA));

            teamPoints.put(winner, teamPoints.get(winner) + 3);
        });

        final List<Team> teamsOrderedByPoints = teamPoints.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(kv -> kv.getKey())
                .collect(Collectors.toList());

        this.winner = Optional.of(teamsOrderedByPoints.get(0));
        this.runnerUp = Optional.of(teamsOrderedByPoints.get(1));
    }

    @Override
    public Optional<Team> getWinner() {
        return winner;
    }

    @Override
    public Optional<Team> getRunnerUp() {
        return runnerUp;
    }
}
