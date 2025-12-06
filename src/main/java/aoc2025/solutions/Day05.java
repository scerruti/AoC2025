package aoc2025.solutions;

import java.util.ArrayList;


public class Day05 extends Day {
    public Day05() { super(5); }

    @Override
    public String part1(ArrayList<String> input) {
        boolean rangesDone = false;
        ArrayList<Range> ranges = new ArrayList<>();
        int numberFresh = 0;

        for (String line : input) {
            if (line.equals("")) {
                rangesDone = true;
                continue;
            }

            if (!rangesDone) {
                try {
                    ranges.add(new Range(line));
                } catch (IllegalArgumentException e) {
                    return "Error";
                }
            } else {
                for (Range range : ranges) {
                    if (range.contains(line)) {
                        // System.out.println(line.trim() + " found in [" + range.getStart() + ", " + range.getEnd() + ")");
                        numberFresh += 1;
                        break;
                    }
                }

            }

        }
        return String.valueOf(numberFresh);
    }

    @Override
    public String part2(ArrayList<String> input) {
        SmartIngredientList ranges = new SmartIngredientList();
        long numberIngredients = 0;

        for (String line : input) {
            if (line.equals("")) {
                break;
            }

            try {
                numberIngredients = numberIngredients + ranges.add(new Range(line));
                // System.out.println("Number of ingredients " + numberIngredients + " after " + line.trim());

            } catch (IllegalArgumentException e) {
                return "Error";
            }

        }
        return String.valueOf(numberIngredients);
    }

}
