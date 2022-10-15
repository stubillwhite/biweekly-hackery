package gameofblobs.internal;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static gameofblobs.internal.testcommon.TestUtils.createBlob;
import static gameofblobs.internal.testcommon.TestUtils.createWorld;
import static org.assertj.core.api.Assertions.assertThat;

public class DisplayTest {

    @Test
    public void displayThenDisplaysWorldToStdout() {
        // Given
        final World world = createWorld(5, 5, Lists.newArrayList(
                createBlob(1, 1, 1),
                createBlob(3, 3, 2)));

        final OutputStream outputStream = redirectStdoutToOutputStream();

        // When
        new Display().display(world);
        restoreStdout();

        // Then
        final List<String> expected = Lists.newArrayList(
                ".. .. .. .. .. ",
                "..  1 .. .. .. ",
                ".. .. .. .. .. ",
                ".. .. ..  2 .. ",
                ".. .. .. .. .. "
        );

        final List<String> actual = Lists.newArrayList(outputStream.toString().split("\n")).subList(0, 5);
        assertThat(actual).isEqualTo(expected);


    }

    private OutputStream redirectStdoutToOutputStream() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        return outputStream;
    }

    private void restoreStdout() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
