package aoc2025.solutions;

public class PatternMatcher {

    /**
     * Hardcoded lookup table for proper divisors.
     * Sorted in DESCENDING order.
     */
    private static final int[][] DIVISORS = {
        {},                     // 0
        {},                     // 1
        {1},                    // 2
        {1},                    // 3
        {2, 1},                 // 4
        {1},                    // 5
        {3, 2, 1},              // 6
        {1},                    // 7
        {4, 2, 1},              // 8
        {3, 1},                 // 9
        {5, 2, 1},              // 10
        {1},                    // 11
        {6, 4, 3, 2, 1},        // 12
        {1},                    // 13
        {7, 2, 1},              // 14
        {5, 3, 1},              // 15
        {8, 4, 2, 1},           // 16
        {1},                    // 17
        {9, 6, 3, 2, 1},        // 18
        {1}                     // 19
    };

    /**
     * Powers of 10 lookup table to avoid Math.pow overhead.
     * index i = 10^i
     */
    private static final long[] POWERS_OF_10 = {
        1L,
        10L,
        100L,
        1000L,
        10000L,
        100000L,
        1000000L,
        10000000L,
        100000000L,
        1000000000L,
        10000000000L,
        100000000000L,
        1000000000000L,
        10000000000000L,
        100000000000000L,
        1000000000000000L,
        10000000000000000L,
        100000000000000000L,
        1000000000000000000L
    };

    /**
     * Checks if the given number 'n' consists of a repeating pattern using primitives only.
     */
    public static boolean hasRepeatingPattern(long n) {
        if (n < 10) return false; 

        // 1. Calculate length efficiently using integer comparison
        // This avoids the overhead of Math.log10 (floating point conversion)
        int len = getLength(n);

        if (len >= DIVISORS.length) return false;

        int[] divs = DIVISORS[len];
        
        // Use a simple boolean flag array to mark failures.
        boolean[] failed = new boolean[len + 1];

        // 2. Iterate through candidates (Primitive Array)
        for (int currentLen : divs) {
            
            // Pruning: If this length was already marked as failed (by a larger multiple), skip it.
            if (failed[currentLen]) continue;

            if (checkPatternMath(n, len, currentLen)) {
                return true; 
            } else {
                // Failure Logic: If 8 fails, mark 4, 2, 1 as failed.
                for (int k = 1; k <= currentLen / 2; k++) {
                    if (currentLen % k == 0) {
                        failed[k] = true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean checkPatternMath(long n, int totalLen, int patLen) {
        long shift = POWERS_OF_10[totalLen - patLen];
        long pattern = n / shift; 
        long mask = POWERS_OF_10[patLen];
        long tempN = n;
        
        int steps = totalLen / patLen;
        
        for (int k = 0; k < steps; k++) {
            long chunk = tempN % mask; 
            if (chunk != pattern) {
                return false;
            }
            tempN /= mask; 
        }
        
        return true;
    }

    /**
     * Returns the number of digits in n (for positive n).
     * Uses binary-search style comparison for speed instead of division or log10.
     */
    private static int getLength(long n) {
        if (n < 10000000000L) { // 10 or less
            if (n < 100000L) { // 5 or less
                if (n < 100L) {
                    return (n < 10L) ? 1 : 2;
                } else {
                    return (n < 1000L) ? 3 : (n < 10000L ? 4 : 5);
                }
            } else { // 6 to 10
                if (n < 10000000L) {
                    return (n < 1000000L) ? 6 : 7;
                } else {
                    return (n < 100000000L) ? 8 : (n < 1000000000L ? 9 : 10);
                }
            }
        } else { // 11 or more
            if (n < 100000000000000L) { // 11 to 14
                if (n < 100000000000L) {
                    return (n < 10000000000L) ? 11 : 12;
                } else {
                    return (n < 1000000000000L) ? 13 : 14;
                }
            } else { // 15 to 19
                if (n < 10000000000000000L) {
                    return (n < 1000000000000000L) ? 15 : 16;
                } else {
                    if (n < 100000000000000000L) return 17;
                    return (n < 1000000000000000000L) ? 18 : 19;
                }
            }
        }
    }
}