package wordfunnel;

import com.google.common.collect.Sets;
import wordfunnel.internal.MaxDepthCalculator;
import wordfunnel.internal.SubwordGenerators.SubwordGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static wordfunnel.internal.StreamUtils.toStream;
import static wordfunnel.internal.SubwordGenerators.DROP_MULTIPLE_CHARS_GENERATOR;
import static wordfunnel.internal.SubwordGenerators.DROP_SINGLE_CHAR_GENERATOR;

public class WordFunnel {
    private static final String FILENAME = "wordfunnel/enable1.txt";

    public static void main(String[] args) {
        new WordFunnel();
    }

    public WordFunnel() {
        final List<String> words = loadWordList(FILENAME);

        partOneBonusOne(words, DROP_SINGLE_CHAR_GENERATOR);
        partOneBonusTwo(words, DROP_SINGLE_CHAR_GENERATOR);

        final MaxDepthCalculator singleCharMaxDepthCalculator = new MaxDepthCalculator(words, DROP_SINGLE_CHAR_GENERATOR);
        partTwoBonusOne(words, singleCharMaxDepthCalculator);

        final MaxDepthCalculator multiCharMaxDepthCalculator = new MaxDepthCalculator(words, DROP_MULTIPLE_CHARS_GENERATOR);
        partTwoBonusTwo(words, multiCharMaxDepthCalculator);
    }

    private void partOneBonusOne(List<String> words, SubwordGenerator generator) {
        final Set<String> wordSet = Sets.newHashSet(words);

        System.out.println("Part one bonus one");
        System.out.println(subwords(wordSet, generator, "dragoon"));
        System.out.println(subwords(wordSet, generator, "boats"));
        System.out.println(subwords(wordSet, generator, "affidavit"));
        System.out.println();
    }

    private void partOneBonusTwo(List<String> words, SubwordGenerator generator) {
        final Set<String> wordSet = Sets.newHashSet(words);

        final List<String> wordsWithFiveSuccessors =
                wordSet.stream()
                        .filter(word -> subwords(wordSet, generator, word).size() == 5)
                        .collect(toList());

        System.out.println("Part one bonus two");
        System.out.println(wordsWithFiveSuccessors.size());
        System.out.println(wordsWithFiveSuccessors);
        System.out.println();
    }

    private void partTwoBonusOne(List<String> words, MaxDepthCalculator calculator) {
        final List<String> wordsWithFunnelLengthOfTen =
                words.parallelStream()
                        .filter(x -> calculator.maxFunnelDepth(x) == 10)
                        .collect(toList());

        System.out.println("Part two bonus one");
        System.out.println(wordsWithFunnelLengthOfTen);
        System.out.println();
    }

    private void partTwoBonusTwo(List<String> words, MaxDepthCalculator calculator) {
        final List<String> wordsWithTwelveSuccessors =
                words.parallelStream()
                        .filter(x -> calculator.maxFunnelDepth(x) == 12)
                        .collect(toList());

        System.out.println("Part two bonus two");
        System.out.println(wordsWithTwelveSuccessors);
        System.out.println();
    }

    private Set<String> subwords(Set<String> wordSet, SubwordGenerator generator, String word) {
        return toStream(generator.subwords(word))
                .filter(wordSet::contains)
                .collect(toSet());
    }

    private static List<String> loadWordList(String filename) {
        final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().collect(toList());
    }
}
