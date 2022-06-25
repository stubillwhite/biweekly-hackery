package worldcup.internal;

import com.google.common.annotations.VisibleForTesting;

import java.util.Random;

public class ELORatingBasedPredictor {

    private final boolean ratingsFrozenWithinTournament;

    private static final int K = 10;

    public interface RandomNumberSource {
        double next();
    }

    private static final RandomNumberSource DEFAULT_RANDOM_NUMBER_SOURCE = new RandomNumberSource() {
        private final Random random = new Random();

        @Override
        public double next() {
            return random.nextDouble();
        }
    };

    public ELORatingBasedPredictor(boolean ratingsFrozenWithinTournament) {
        this(ratingsFrozenWithinTournament, DEFAULT_RANDOM_NUMBER_SOURCE);
    }

    @VisibleForTesting
    ELORatingBasedPredictor(boolean ratingsFrozenWithinTournament,
                            RandomNumberSource randomNumberSource) {
        this.ratingsFrozenWithinTournament = ratingsFrozenWithinTournament;
        this.randomNumberSource = randomNumberSource;
    }

    private final RandomNumberSource randomNumberSource;

    public Team predictWinner(Team a, Team b) {
        return randomNumberSource.next() < probabiltyOfWinning(a, b) ? a : b;
    }

    public int newRating(Team a, Team b, boolean isWin) {
        if (ratingsFrozenWithinTournament) {
            return a.getRating();
        }
        else {
            /*
             * elo_{new} = elo_{old} + KG(W-W_e)
             * Where
             *   K   = importance
             *   G   = goal difference (assumed to be 1)
             *   W   = 1 if won, 0 if lost
             *   W_e = probability of winning
             */

            final int goalDifference = 1;
            final int w = isWin ? 1 : 0;

            final double newRating = a.getRating() + (K * goalDifference * (w - probabiltyOfWinning(a, b)));

            return Long.valueOf(Math.round(newRating)).intValue();
        }
    }

    private double probabiltyOfWinning(Team a, Team b) {
        /*
         * 1/(1+10^m) where m is the rating difference (rating(B)-rating(A)) divided by 400
         */
        final int ratingDifference = b.getRating() - a.getRating();
        final double m = ratingDifference / 400.0;
        final double probability = 1.0 / (1 + Math.pow(10.0, m));

        return probability;
    }
}
