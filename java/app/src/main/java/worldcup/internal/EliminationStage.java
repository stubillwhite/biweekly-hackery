package worldcup.internal;

import java.util.List;
import java.util.Optional;

public class EliminationStage implements Stage {

    private Optional<Team> winner = Optional.empty();
    private Optional<Team> runnerUp = Optional.empty();

    private final Stage subStageA;
    private final Stage subStageB;

    public EliminationStage(EliminationStage a, EliminationStage b) {
        subStageA = a;
        subStageB = b;
    }

    public EliminationStage(GroupStage a, GroupStage b) {
        subStageA = a;
        subStageB = b;
    }

    @Override
    public List<Optional<Team>> getTeams() {
        if (subStageA instanceof GroupStage) {
            return List.of(subStageA.getWinner(), subStageB.getRunnerUp());
        }
        else {
            return List.of(subStageA.getWinner(), subStageB.getWinner());
        }
    }

    @Override
    public void predictOutcome(ELORatingBasedPredictor predictor) {
        if (getTeams().contains(Optional.empty())) {
            subStageA.predictOutcome(predictor);
            subStageB.predictOutcome(predictor);
        }

        final Team a = getTeams().get(0).get();
        final Team b = getTeams().get(1).get();

        final Team winner = predictor.predictWinner(a, b);

        final boolean isWinForA = a == winner;

        a.setRating(predictor.newRating(a, b, isWinForA));
        b.setRating(predictor.newRating(b, a, !isWinForA));

        this.winner = Optional.of(isWinForA ? a : b);
        this.runnerUp = Optional.of(isWinForA ? b : a);
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
