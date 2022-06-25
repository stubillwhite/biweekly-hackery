package worldcup.internal;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Combinatorics {

    /**
     * Given a list of items, return all unique selections of N items from that list.
     *
     * @param n     The number of items to select, which should be greater than zero and less than or equal to the length of the list
     * @param items The list of items
     * @param <T>   The type of the items
     * @return The unique selections of N items frm the list
     */
    public static <T> List<List<T>> selectionsOfNFromList(int n, List<T> items) {
        if (n == items.size()) {
            return Arrays.asList(Lists.newArrayList(items));
        } else if (n == 0) {
            return Arrays.asList(Lists.newArrayList());
        } else {
            final List<List<T>> notIncludingFirstItem = selectionsOfNFromList(n, items.subList(1, items.size()));

            final List<List<T>> includingFirstItem = selectionsOfNFromList(n - 1, items.subList(1, items.size())).stream()
                    .map(selections -> {
                                selections.add(0, items.get(0));
                                return selections;
                            }
                    )
                    .collect(Collectors.toList());

            final List<List<T>> allSelections = Lists.newArrayList();
            allSelections.addAll(includingFirstItem);
            allSelections.addAll(notIncludingFirstItem);
            return allSelections;
        }
    }
}
