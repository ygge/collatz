import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Collatz {

    private static final long MAX = 100000000L;
    private static final long STORE_MAX = 10000000L;

    public static void main(String[] args) {
        Map<Long, Result> cache = new HashMap<>();
        Set<Long> seen = new HashSet<>();
        cache.put(1L, new Result(0, 1));
        seen.add(1L);

        long maxSteps = 0;
        long maxHeight = 0;
        long stepsIndex = 0;
        long heightIndex = 0;
        int goal = 10;
        long start = System.nanoTime();
        for (long i = 2; i <= MAX; ++i) {
            if (i == goal) {
                System.out.printf(" <%9d %8d %5d %9d %15d in %d ms%n", i, stepsIndex, maxSteps, heightIndex, maxHeight, (System.nanoTime()-start)/1000000);
                maxSteps = maxHeight = stepsIndex = heightIndex = 0;
                goal *= 10;
                start = System.nanoTime();
            }
            var result = calc(i, cache, seen);
            if (result.steps > maxSteps) {
                maxSteps = result.steps;
                stepsIndex = i;
            }
            if (result.max > maxHeight) {
                maxHeight = result.max;
                heightIndex = i;
            }
        }
    }

    private static Result calc(long i, Map<Long, Result> cache, Set<Long> seen) {
        if (cache.containsKey(i)) {
            return cache.get(i);
        }
        if (i <= STORE_MAX && !seen.add(i)) {
            throw new IllegalStateException(String.format("Loop detected for i=%d", i));
        }
        var ii = (i&1) == 1 ? 3*i+1 : i/2;
        var result = calc(ii, cache, seen).update(i);
        if (i <= STORE_MAX) {
            cache.put(i, result);
        }
        return result;
    }

    private static class Result {

        private final long steps, max;

        private Result(long steps, long max) {
            this.steps = steps;
            this.max = max;
        }

        public Result update(long i) {
            return new Result(steps + 1, Math.max(max, i));
        }
    }
}
