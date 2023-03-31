package worldcup.internal;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static worldcup.internal.Combinatorics.selectionsOfNFromList;

public class CombinatoricsTest {

    private static final String A = "A";
    private static final String B = "B";
    private static final String C = "C";

    @Test
    public void selectionsOfNFromListGivenNEqualsOneThenReturnsListsOfOneItem() {
        assertThat(selectionsOfNFromList(1, List.of(A, B, C)))
                .containsExactly(List.of(A), List.of(B), List.of(C));
    }

    @Test
    public void selectionsOfNFromListGivenNLessThanListLengthThenReturnsCombinations() {
        assertThat(selectionsOfNFromList(2, List.of(A, B, C)))
                .containsExactly(List.of(A, B), List.of(A, C), List.of(B, C));
    }

    @Test
    public void selectionsOfNFromListGivenNEqualsListLengthThenReturnsLists() {
        assertThat(selectionsOfNFromList(3, List.of(A, B, C)))
                .containsExactly(List.of(A, B, C));
    }

}
