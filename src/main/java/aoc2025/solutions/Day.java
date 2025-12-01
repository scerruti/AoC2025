package aoc2025.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class Day {
    private final int dayNumber;

    public Day(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public List<String> getInputLines() {
        String filename = String.format("resources/day%02d.txt", dayNumber);
        try {
            return Files.readAllLines(Path.of(filename));
        } catch (IOException e) {
            throw new RuntimeException("Could not read input file: " + filename, e);
        }
    }

    public abstract String part1(List<String> input);
    public abstract String part2(List<String> input);

    public void solve() {
        List<String> input = getInputLines();
        long start1 = System.currentTimeMillis();
        String result1 = part1(input);
        long time1 = System.currentTimeMillis() - start1;
        System.out.printf("Day %02d - Part 1: %s (%d ms)%n", dayNumber, result1, time1);

        long start2 = System.currentTimeMillis();
        String result2 = part2(input);
        long time2 = System.currentTimeMillis() - start2;
        System.out.printf("Day %02d - Part 2: %s (%d ms)%n", dayNumber, result2, time2);
    }
}
