package aoc2025;

import aoc2025.solutions.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        int day;
        if (args.length > 0) {
            day = Integer.parseInt(args[0]);
        } else {
            LocalDate now = LocalDate.now();
            // December 1-12 only
            if (now.getMonthValue() == 12 && now.getDayOfMonth() >= 1 && now.getDayOfMonth() <= 12) {
                day = now.getDayOfMonth();
            } else {
                System.out.println("Not an AoC day (December 1-12). Specify a day as an argument.");
                return;
            }
        }

        Day solution = getDayInstance(day);
        if (solution != null) {
            solution.solve();
        } else {
            System.out.println("No solution implemented for Day " + day);
        }
    }

    private static Day getDayInstance(int day) {
        return switch (day) {
            case 1 -> new Day01();
            case 2 -> new Day02();
            case 3 -> new Day03();
            case 4 -> new Day04();
            case 5 -> new Day05();
            case 6 -> new Day06();
            case 7 -> new Day07();
            case 8 -> new Day08();
            case 9 -> new Day09();
            case 10 -> new Day10();
            case 11 -> new Day11();
            case 12 -> new Day12();
            default -> null;
        };
    }
}
