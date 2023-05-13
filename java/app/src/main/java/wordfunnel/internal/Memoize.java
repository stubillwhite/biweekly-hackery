package wordfunnel.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoize<T, R> {

    private final Map<T, R> cache;

    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        return new Memoize<T, R>().memoizeFunction(function);
    }

    private Memoize() {
        this.cache = new ConcurrentHashMap<>();
    }

    private Function<T, R> memoizeFunction(Function<T, R> f) {
        return x -> {
            if (!cache.containsKey(x)) {
                final R result = f.apply(x);
                cache.put(x, result);
            }
            return cache.get(x);
        };
    }
}
