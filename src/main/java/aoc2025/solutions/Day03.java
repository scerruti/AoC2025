package aoc2025.solutions;

import java.util.ArrayList;

public class Day03 extends Day {
    public Day03() { super(3); }

    @Override
    public String part1(ArrayList<String> input) {
        int joltage = 0;
        for (String bank : input) {
            int j = Bank.maxJoltage(bank);
            System.out.println(j);
            joltage += j;
        }
        return String.valueOf(joltage);
    }

    @Override
    public String part2(ArrayList<String> input) {
        long joltage = 0;
        for (String bank : input) {
            long j = Bank.maxJoltage(bank, 12);
            System.out.println(j);
            joltage += j;
        }
        return String.valueOf(joltage);
    }
}
