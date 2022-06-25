package worldcup.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ELORatingBasedPredictorTest {

    private final Team teamA = new Team("team-a", 2400);
    private final Team teamB = new Team("team-b", 2000);

    @Test
    public void predictWinnerGivenRandomNumberLessThanProbabilityOfWinningThenReturnsTeamA() {
        // Given
        final ELORatingBasedPredictor calculator = new ELORatingBasedPredictor(false, randomNumberSourceReturning(0.90));

        // When, Then
        assertThat(calculator.predictWinner(teamA, teamB)).isEqualTo(teamA);
    }

    @Test
    public void predictWinnerGivenRandomNumberGreaterThanProbabilityOfWinningThenReturnsTeamB() {
        // Given
        final ELORatingBasedPredictor calculator = new ELORatingBasedPredictor(false, randomNumberSourceReturning(0.95));

        // When, Then
        assertThat(calculator.predictWinner(teamA, teamB)).isEqualTo(teamB);
    }

    @Test
    public void newRatingGivenRatingsNotFrozenThenReturnsNewRatingAccordingToELOFormula() {
        // Given
        final ELORatingBasedPredictor calculator = new ELORatingBasedPredictor(false);

        // When, Then
        assertThat(calculator.newRating(teamA, teamB, true)).isEqualTo(2401);
        assertThat(calculator.newRating(teamB, teamA, false)).isEqualTo(1999);
        assertThat(calculator.newRating(teamA, teamB, false)).isEqualTo(2391);
        assertThat(calculator.newRating(teamB, teamA, true)).isEqualTo(2009);
    }

    @Test
    public void newRatingGivenRatingsFrozenThenReturnsExistingRating() {
        // Given
        final ELORatingBasedPredictor calculator = new ELORatingBasedPredictor(true);

        // When, Then
        assertThat(calculator.newRating(teamA, teamB, true)).isEqualTo(teamA.getRating());
        assertThat(calculator.newRating(teamB, teamA, false)).isEqualTo(teamB.getRating());
    }

    private ELORatingBasedPredictor.RandomNumberSource randomNumberSourceReturning(double x) {
        return () -> x;
    }
}
