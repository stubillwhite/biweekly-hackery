package worldcup;

import com.google.common.collect.Maps;
import worldcup.internal.*;

import java.util.*;
import java.util.stream.Collectors;

public class WorldCupPrognostication {

    private static final Team ENGLAND = new Team("England", 1692);
    private static final Team GERMANY = new Team("Germany", 1775);
    private static final Team NETHERLANDS = new Team("Netherlands", 1726);
    private static final Team DENMARK = new Team("Denmark", 1503);
    private static final Team NORWAY = new Team("Norway", 1537);
    private static final Team SWEDEN = new Team("Sweden", 1801);
    private static final Team FRANCE = new Team("France", 1832);
    private static final Team BELGIUM = new Team("Belgium", 1374);
    private static final Team ICELAND = new Team("Iceland", 1476);
    private static final Team SPAIN = new Team("Spain", 1668);
    private static final Team FINLAND = new Team("Finland", 1210);
    private static final Team AUSTRIA = new Team("Austria", 1380);
    private static final Team ITALY = new Team("Italy", 1582);
    private static final Team SWITZERLAND = new Team("Switzerland", 1327);
    private static final Team NORTHERN_IRELAND = new Team("Northern Ireland", 980);
    private static final Team PORTUGAL = new Team("Portugal", 1248);

    public static void main(String[] args) {
        final Map<String, Integer> wins = Maps.newHashMap();

        for (int i = 0; i < 1000000; i++) {
            final Team winner = runSimulation();
            wins.put(winner.getName(), 1 + wins.getOrDefault(winner.getName(), 0));
        }

        final List<Map.Entry<String, Integer>> teamsOrderedByNumberOfWins = wins.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toList());

        teamsOrderedByNumberOfWins.forEach(entry -> {
            System.out.printf("%10d %s\n", entry.getValue(), entry.getKey());
        });
    }

    private static Team runSimulation() {
        final GroupStage groupStageA = groupStage(ENGLAND, AUSTRIA, NORWAY, NORTHERN_IRELAND);
        final GroupStage groupStageB = groupStage(GERMANY, DENMARK, SPAIN, FINLAND);
        final GroupStage groupStageC = groupStage(NETHERLANDS, SWEDEN, SWITZERLAND, PORTUGAL);
        final GroupStage groupStageD = groupStage(FRANCE, ITALY, BELGIUM, ICELAND);

        final Stage tournament =
                eliminationStage(
                        eliminationStage(
                                eliminationStage(groupStageD, groupStageC),
                                eliminationStage(groupStageC, groupStageD)),
                        eliminationStage(
                                eliminationStage(groupStageB, groupStageA),
                                eliminationStage(groupStageA, groupStageB)));

        tournament.predictOutcome(new ELORatingBasedPredictor(true));
        return tournament.getWinner().get();
    }

    private static EliminationStage eliminationStage(GroupStage a, GroupStage b) {
        return new EliminationStage(a, b);
    }

    private static EliminationStage eliminationStage(EliminationStage a, EliminationStage b) {
        return new EliminationStage(a, b);
    }

    private static GroupStage groupStage(Team... teams) {
        return new GroupStage(teams);
    }
}
