package cribbage.internal;

import org.junit.jupiter.api.Test;

import static cribbage.internal.DisplayUtils.toHand;
import static org.assertj.core.api.Assertions.assertThat;


public class HandScorerTest {

    private final HandScorer handScorer = new HandScorer();

    @Test
    public void scoreFifteensGivenNoneExistThenScoresZero() {
        // Given, When
        final long actual = handScorer.scoreFifteens(toHand("AC,AD,AH,AS,2C"));

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void scoreFifteensGivenFourExistThenScoresEight() {
        // Given, When
        final long actual = handScorer.scoreFifteens(toHand("TD,6C,4S,5H,5D"));

        // Then
        assertThat(actual).isEqualTo(8);
    }

    @Test
    public void scoreFlushesGivenNoneExistThenScoresZero() {
        // Given, When
        final long actual = handScorer.scoreFlushes(toHand("AC,AD,AH,AS,2C"));

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void scoreFlushesGivenFourCardFlushThenScoresFour() {
        // Given, When
        final long actual = handScorer.scoreFlushes(toHand("AC,2C,3C,4C,AD"));

        // Then
        assertThat(actual).isEqualTo(4);
    }

    @Test
    public void scoreFlushesGivenFourCardFlushUsingFaceUpCardThenScoresZero() {
        // Given, When
        final long actual = handScorer.scoreFlushes(toHand("AC,2C,3C,AD,4C"));

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void scoreFlushesGivenFiveCardFlushThenScoresFive() {
        // Given, When
        final long actual = handScorer.scoreFlushes(toHand("AC,2C,3C,4C,5C"));

        // Then
        assertThat(actual).isEqualTo(5);
    }

    @Test
    public void scoreRunsGivenNoneExistThenScoresZero() {
        // Given, When
        final long actual = handScorer.scoreRuns(toHand("AC,AD,AS,AH,2C"));

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void scoreRunsGivenRunsOfThreeExistThenScoresThree() {
        // Given, When
        final long actual = handScorer.scoreRuns(toHand("AC,AD,2S,3H,KC"));

        // Then
        assertThat(actual).isEqualTo(3);
    }

    @Test
    public void scoreRunsGivenRunsOfFourExistThenScoresFour() {
        // Given, When
        final long actual = handScorer.scoreRuns(toHand("AC,AD,2S,3H,4C"));

        // Then
        assertThat(actual).isEqualTo(4);
    }

    @Test
    public void scoreRunsGivenRunOfFiveExistsThenScoresFive() {
        // Given, When
        final long actual = handScorer.scoreRuns(toHand("AC,2D,3S,4H,5C"));

        // Then
        assertThat(actual).isEqualTo(5);
    }

    @Test
    public void scorePairsGivenNoneExistThenScoresZero() {
        // Given, When
        final long actual = handScorer.scorePairs(toHand("AC,2C,3C,4C,5C"));

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void scorePairsGivenTwoPairsExistThenScoresFour() {
        // Given, When
        final long actual = handScorer.scorePairs(toHand("AC,AD,2C,2D,3C"));

        // Then
        assertThat(actual).isEqualTo(4);
    }

    @Test
    public void scorePairsGivenPairRoyalExistThenScoresSix() {
        // Given, When
        final long actual = handScorer.scorePairs(toHand("AC,AD,AH,2C,3C"));

        // Then
        assertThat(actual).isEqualTo(6);
    }

    @Test
    public void scorePairsGivenDoublePairRoyalExistThenScoresTwelve() {
        // Given, When
        final long actual = handScorer.scorePairs(toHand("AC,AD,AH,AS,2C"));

        // Then
        assertThat(actual).isEqualTo(12);
    }

    @Test
    public void scoreNobsGivenNoneExistThenScoresZero() {
        // Given, When
        final long actual = handScorer.scoreNobs(toHand("AC,JD,JH,JS,AC"));

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void scoreNobsGivenOneExistsThenScoresOne() {
        // Given, When
        final long actual = handScorer.scoreNobs(toHand("JC,JD,JH,JS,AC"));

        // Then
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void scoreHandGivenExampleHandThenScoresExpectedValue() {
        assertExampleHandHasScore("5D,QS,JC,KH,AC", 10);
        assertExampleHandHasScore("8C,AD,TC,6H,7S", 7);
        assertExampleHandHasScore("AC,6D,5C,TC,8C", 4);
    }

    private void assertExampleHandHasScore(String exampleHand, int expectedValue) {
        final Hand hand = toHand(exampleHand);
        final long actual = handScorer.scoreHand(hand);
        assertThat(actual).isEqualTo(expectedValue);
    }
}
