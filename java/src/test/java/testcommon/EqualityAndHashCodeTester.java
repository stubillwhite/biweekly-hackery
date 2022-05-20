package testcommon;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualityAndHashCodeTester {

    public static <T> void testEqualityAndHashCode(List<T> equalObjects,
                                                   List<T> differentObjects) {
        testEquality(equalObjects);
        testInequality(differentObjects);
        testHashCode(equalObjects);
    }

    private static final class DifferentClass {
    }

    private static <T> void testEquality(List<T> objects) {
        objects.forEach(objectA -> {
            objects.forEach(objectB -> {
                assertThat(objectA).isEqualTo(objectB);
            });
        });
    }

    private static <T> void testInequality(List<T> objects) {
        objects.forEach(objectA -> {
            objects.stream()
                    .filter(objectB -> objectB != objectA)
                    .forEach(objectB -> assertThat(objectA).isNotEqualTo(objectB));
        });

        assertThat(objects.get(0)).isNotEqualTo(null);
        assertThat(objects.get(0)).isNotEqualTo(new DifferentClass());
    }

    private static <T> void testHashCode(List<T> objects) {
        objects.forEach(objectA -> {
            objects.forEach(objectB -> {
                assertThat(objectA.hashCode()).isEqualTo(objectB.hashCode());
            });
        });
    }
}
