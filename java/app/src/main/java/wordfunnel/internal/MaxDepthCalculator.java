package wordfunnel.internal;

import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static wordfunnel.internal.StreamUtils.toStream;
import static wordfunnel.internal.SubwordGenerators.SubwordGenerator;

public class MaxDepthCalculator {

    private final Map<String, String> words;
    private final SubwordGenerator generator;

    public MaxDepthCalculator(List<String> words, SubwordGenerator generator) {
        this.words = words.stream().collect(toMap(Function.identity(), Function.identity()));
        this.generator = generator;
    }

    public Integer maxFunnelDepth(String word) {
        return memoizedMaxFunnelDepth.apply(word);
    }

    private final Function<String, Integer> memoizedMaxFunnelDepth = Memoize.memoize(this::nonMemoizedMaxFunnelDepth);

    private Integer nonMemoizedMaxFunnelDepth(String word) {
        final Iterator<String> nextWords = validSubwords(word);

        if (!nextWords.hasNext()) {
            return 1;
        } else {
            return toStream(nextWords)
                    .map(x -> 1 + memoizedMaxFunnelDepth.apply(x))
                    .max(Integer::compareTo)
                    .get();
        }
    }

    private Iterator<String> validSubwords(String word) {
        return Iterators.filter(generator.subwords(word), words::containsKey);
    }
}
