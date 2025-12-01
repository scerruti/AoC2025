package aoc2025.solutions;

import java.util.ArrayList;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public abstract class Day {
    private final int dayNumber;

    public Day(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public ArrayList<String> getInputLines() {
        String filename = String.format("/day%02d.txt", dayNumber);
        try (InputStream is = getClass().getResourceAsStream(filename)) {
            if (is == null) throw new IOException("Input file not found: " + filename);
            return new ArrayList<>(new BufferedReader(new InputStreamReader(is)).lines().toList());
        } catch (IOException e) {
            throw new RuntimeException("Could not read input file: " + filename, e);
        }
    }
    protected ArrayList<String> loadInput(int day) {
        String sampleFlag = System.getProperty("aoc.sample");
        if (sampleFlag == null) {
            sampleFlag = System.getenv("AOC_SAMPLE");
        }
        String filename = String.format("/day%02d%s.txt", day, (sampleFlag != null && sampleFlag.equalsIgnoreCase("true")) ? "-sample" : "");
        try (InputStream is = getClass().getResourceAsStream(filename)) {
            if (is == null) throw new IOException("Input file not found: " + filename);
            return new ArrayList<>(new BufferedReader(new InputStreamReader(is)).lines().toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String part1(ArrayList<String> input);
    public abstract String part2(ArrayList<String> input);

    public void solve() {
        ArrayList<String> input;
        String sampleFlag = System.getProperty("aoc.sample");
        if (sampleFlag == null) {
            sampleFlag = System.getenv("AOC_SAMPLE");
        }
        if (sampleFlag != null && sampleFlag.equalsIgnoreCase("true")) {
            input = loadInput(dayNumber);
        } else {
            input = getInputLines();
        }
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
