package aoc2025.solutions;

import java.util.List;

public class Day01 extends Day {
    public Day01() { super(1); }

    @Override
    public String part1(List<String> input) {
        int position = 50;
        int count = 0;
        for (String line : input) {
            int clicks = Integer.parseInt(line.substring(1));
            if (line.substring(0,1).equals("L")) {
                clicks = -clicks;   
            }

            position += clicks;
            while (position < 0) {
                position += 100;
            } 
            
            while (position >= 100) {
                position -= 100;
            }
            
            if (position == 0) {
                count++;
            }

        }
        return Integer.toString(count);
    }

    @Override
    public String part2(List<String> input) {
        int position = 50;
        int count = 0;
        for (String line : input) {
            int clicks = Integer.parseInt(line.substring(1));
            count += clicks / 100;
            clicks = clicks % 100;
            if (line.substring(0,1).equals("L")) {
                clicks = -clicks;   
            }

            int oldPosition = position;
            position += clicks;
            System.out.println("After move of: " + clicks + " position: " + position);
            if (position < 0 ) {
                position += 100;
                if (oldPosition > 0) count++;
                System.out.println("Wrapped up to: " + position);
            } else if (position >= 100) {
                position -= 100;
                count++;
                System.out.println("Wrapped down to: " + position);
            } else if (position > 0 && oldPosition < 0) {
                count++;
                System.out.println("Wrapped up to: " + position);
            } else if (position == 0) {
                count++;
            }

            System.out.println("Current position: " + position + " count: " + count);

        }
        return Integer.toString(count);    }
}
