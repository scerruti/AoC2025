package aoc2025.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class WriteupUtil {
    private static final Map<Integer, String> TITLES = Map.ofEntries(
        Map.entry(1, "Calorie Counting"),
        Map.entry(2, "Rock Paper Scissors"),
        Map.entry(3, "Rucksack Reorganization"),
        Map.entry(4, "Camp Cleanup"),
        Map.entry(5, "Supply Stacks"),
        Map.entry(6, "Tuning Trouble"),
        Map.entry(7, "No Space Left on Device"),
        Map.entry(8, "Treetop Tree House"),
        Map.entry(9, "Rope Bridge"),
        Map.entry(10, "Cathode-Ray Tube"),
        Map.entry(11, "Unknown Title"),
        Map.entry(12, "Hill Climbing Algorithm")
    );

    public static void createWriteup(int year, int day) throws IOException {
        String title = TITLES.getOrDefault(day, "Puzzle");
        String url = String.format("https://adventofcode.com/%d/day/%d", year, day);
        String filename = String.format("writeups/Day%02d.md", day);
        try (FileWriter writer = new FileWriter(filename, false)) {
            writer.write(String.format("# [--- Day %d: %s ---](%s)\n\nSummary, approach, and notes for Day %d will go here.\n", day, title, url, day));
        }
        System.out.printf("Created writeup: %s\n", filename);
    }
}
