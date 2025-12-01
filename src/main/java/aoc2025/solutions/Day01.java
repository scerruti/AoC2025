package aoc2025.solutions;

import java.util.List;

/**
 * Solution for Advent of Code 2025 - Day 1.
 * <p>
 * Part 1: Simulates a dial with 100 positions, counting how many times it lands on 0.
 * Part 2: Handles wraparounds and counts additional events when crossing zero.
 */
public class Day01 extends Day {
    /**
     * Constructs the Day 1 solution.
     */
    public Day01() {
        super(1);
    }

    @Override
    /**
     * Solves Part 1: Simulates a dial with 100 positions, starting at 50.
     * Each line indicates a direction ('L' or 'R') and a number of clicks.
     * Counts how many times the dial lands exactly on position 0 after a move.
     *
     * @param input List of input strings, each representing a move
     * @return the number of times the dial lands on 0
     */
    public String part1(List<String> input) {
        int position = 50;
        int count = 0;
        for (String line : input) {
            // Parse the number of clicks from the input
            int clicks = Integer.parseInt(line.substring(1));
            // Determine direction
            if (line.substring(0, 1).equals("L")) {
                clicks = -clicks;
            }

            position += clicks;
            // Wrap position to [0, 99]
            while (position < 0) {
                position += 100;
            }
            while (position >= 100) {
                position -= 100;
            }

            // Count if landed on 0
            if (position == 0) {
                count++;
            }
        }
        return Integer.toString(count);
    }

    @Override
    /**
     * Solves Part 2: Handles wraparounds and counts additional events when crossing zero.
     * For each move, counts how many times the dial passes or lands on 0, including full rotations.
     *
     * @param input List of input strings, each representing a move
     * @return the total number of times the dial passes or lands on 0
     */
    public String part2(List<String> input) {
        int position = 50;
        int count = 0;
        for (String line : input) {
            // Parse the number of clicks from the input
            int clicks = Integer.parseInt(line.substring(1));

            // Add to count for every complete 100-click rotation (each time the dial passes zero)
            count += clicks / 100;
            // Reduce clicks to the remainder after full rotations, so only the partial move is processed below
            clicks = clicks % 100;

            // Determine direction
            if (line.substring(0, 1).equals("L")) {
                clicks = -clicks;
            }

            int oldPosition = position;
            position += clicks;

            // Handle wraparounds and zero crossings
            if (position == 0) {
                count++;
            } else if (position < 0 && oldPosition > 0) {
                position += 100;
                count++;
            } else if (position < 0) {
                position += 100;
            } else if (position >= 100) {
                position -= 100;
                count++;
            } else if (position > 0 && oldPosition < 0) {
                count++;
            }

        }

        return Integer.toString(count);
    }
}
