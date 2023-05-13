package wordfunnel.internal;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static wordfunnel.internal.StreamUtils.toStream;

public class SubwordGenerators {

    private SubwordGenerators() {
    }

    public static SubwordGenerator DROP_SINGLE_CHAR_GENERATOR = new DropSingleCharGenerator();
    public static SubwordGenerator DROP_MULTIPLE_CHARS_GENERATOR = new DropMultipleCharsGenerator();

    public interface SubwordGenerator {
        Iterator<String> subwords(String word);
    }

    private static class DropSingleCharGenerator implements SubwordGenerator {

        @Override
        public Iterator<String> subwords(String word) {
            final int length = word.length();

            return IntStream.range(0, length)
                    .mapToObj(x -> word.substring(0, x) + word.substring(x + 1, length))
                    .filter(x -> !x.equals(word))
                    .distinct()
                    .iterator();
        }
    }

    private static class DropMultipleCharsGenerator implements SubwordGenerator {
        @Override
        public Iterator<String> subwords(String word) {
            final Iterator<String> subwords = subsetsOf(word);
            return Iterators.filter(subwords, x -> !x.equals(word));
        }

        private Iterator<String> subsetsOf(String s) {
            if (s.isEmpty()) {
                return Lists.newArrayList(s).iterator();
            } else {
                final Iterator<String> withoutFirstChar = subsetsOf(s.substring(1));
                return toStream(withoutFirstChar)
                        .flatMap(x -> Stream.of(x, s.charAt(0) + x))
                        .iterator();
            }
        }
    }
}
