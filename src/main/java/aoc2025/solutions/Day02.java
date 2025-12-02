
package aoc2025.solutions;

import java.util.ArrayList;

/**
 * Solution for Advent of Code 2025 Day 2.
 * <p>
 * Note: This solution uses long and Long.parseLong due to input values exceeding int range.
 * This is an exception to the AP CSA Java subset, which only allows int/Integer.parseInt.
 * All other code is AP CSA compliant.
 */
public class Day02 extends Day {

    /**
     * Constructs the Day 2 solution object.
     */
    public Day02() {
        super(2);
    }

    /**
     * Part 1: Sums all numbers in the input ranges where the number has even length
     * and the first half of its digits match the second half (e.g., 1212, 3434).
     *
     * @param input ArrayList of input lines (expects a single line of comma-separated ranges)
     * @return The sum as a String
     */
    @Override
    public String part1(ArrayList<String> input) {
        long sum = 0;

        // Separate the ranges by comma
        String[] ranges = input.get(0).split(",");

        for (String range : ranges) {
            // Split each range into lower and upper bounds
            String[] bounds = range.split("-");
            long lower = Long.parseLong(bounds[0]); // AP CSA exception: must use long
            long upper = Long.parseLong(bounds[1]);

            // Iterate through all numbers in the range
            for (long i = lower; i <= upper; i++) {
                String s = String.valueOf(i);

                // Skip numbers with odd length
                if (s.length() % 2 != 0) continue;

                int halfWidth = s.length() / 2;
                boolean allMatch = true;

                // Compare first half and second half using substring
                for (int j = 0; j < halfWidth; j++) {
                    String left = s.substring(j, j + 1);
                    String right = s.substring(j + halfWidth, j + halfWidth + 1);
                    if (!left.equals(right)) {
                        allMatch = false;
                        break;
                    }
                }

                if (allMatch) {
                    sum += i;
                }
            }
        }

        return String.valueOf(sum);
    }

    /**
     * Part 2: Sums all numbers in the input ranges where the number consists of repeated patterns
     * of any length (e.g., 1212, 343434, 123123123).
     *
     * @param input ArrayList of input lines (expects a single line of comma-separated ranges)
     * @return The sum as a String
     */
    @Override
    public String part2(ArrayList<String> input) {
        long sum = 0;

        // Separate the ranges by comma
        String[] ranges = input.get(0).split(",");

        for (String range : ranges) {
            // Split each range into lower and upper bounds
            String[] bounds = range.split("-");
            long lower = Long.parseLong(bounds[0]); // AP CSA exception: must use long
            long upper = Long.parseLong(bounds[1]);

            sum += InvalidIdCalculator.sumInvalidIds(lower, upper);

            // Iterate through all numbers in the range
            // for (long i = lower; i <= upper; i++) {
            //     if (PatternMatcher.hasRepeatingPattern(i)) {
            //         sum += i;
            //         continue; // Only count each number once
            //     }
                // String s = String.valueOf(i);
                // int l = s.length();

                // // Try all possible pattern lengths
                // for (int patternLength = 1; patternLength <= l / 2; patternLength++) {
                //     // Only consider pattern lengths that divide the string evenly
                //     if (l % patternLength != 0) {
                //         continue;
                //     }

                //     String pattern = s.substring(0, patternLength);
                //     boolean allMatch = true;

                //     // Check if the pattern repeats throughout the string
                //     for (int j = 1; j < l / patternLength; j++) {
                //         if (!pattern.equals(s.substring(j * patternLength, (j + 1) * patternLength))) {
                //             allMatch = false;
                //             break;
                //         }
                //     }

                //     // Add and break if a repeated pattern is found
                //     if (allMatch) {
                //         sum += i;
                //         break; // Only count each number once
                //     }
                // }
            // }
        }

        return String.valueOf(sum);
    }
}
