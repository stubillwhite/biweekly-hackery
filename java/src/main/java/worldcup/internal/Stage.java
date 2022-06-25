package worldcup.internal;

import java.util.List;
import java.util.Optional;

public interface Stage {

    List<Optional<Team>> getTeams();

    void predictOutcome(ELORatingBasedPredictor ELORating);

    Optional<Team> getWinner();

    Optional<Team> getRunnerUp();
}
