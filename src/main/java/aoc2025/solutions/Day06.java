package aoc2025.solutions;

import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.stream.Collectors;

public class Day06 extends Day {
    public Day06() {
        super(6);
    }

    @Override
    public String part1(ArrayList<String> input) {
        int rowCount = input.size() - 1;
        int problemCount = input.get(0).replaceAll("\\s+", " ").trim().split(" ").length;

        long[][] problems = new long[problemCount][rowCount];
        for (int i = 0; i < rowCount; i++) {
            // System.out.print(input.get(i) + ": ");
            // I fell into a trap here if trying to split by " " but there are multiple
            // spaces
            // I could have avoided the regular expression by reading the data with Scanner
            // I also did not observe I needed to trim the string before processing to
            // remove leading spaces
            String normalizedLine = input.get(i).replaceAll("\\s+", " ").trim();
            // System.out.print(normalizedLine + ": ");
            // System.out.print("[" + problems.length + "] ");
            String[] numbersInRow = normalizedLine.split(" ");
            for (int j = 0; j < problemCount; j++) {
                problems[j][i] = Long.parseLong(numbersInRow[j]);
                // System.out.print("|" + problems[j][i] + "| ");
            }
            // System.out.println();
        }
        String[] operators = input.get(rowCount).replaceAll("\\s+", " ").trim().split(" ");
        // System.out.println(Arrays.toString(operators));

        long grandTotal = 0;
        for (int i = 0; i < problemCount; i++) {
            long result = 0;
            if (operators[i].equals("*")) {
                result = 1;
            }

            for (int j = 0; j < rowCount; j++) {
                if (operators[i].equals("*")) {
                    result *= problems[i][j];
                } else {
                    result += problems[i][j];
                }
            }
            // System.out.println(Arrays.stream(problems[i])
            // .mapToObj(String::valueOf)
            // .collect(Collectors.joining(" "+operators[i]+" ")) + " = " + result);
            grandTotal += result;
        }

        return String.valueOf(grandTotal);
    }

    @Override
    public String part2(ArrayList<String> input) {
        int rowCount = input.size() - 1;

        // Spaces are now important. It seems like we need to determine the number of
        // columns each row
        // uses to represent a number. We'll use the operands row to determine the
        // number of problems for safety.
        String[] operators = input.get(rowCount).replaceAll("\\s+", " ").trim().split(" ");
        int problemCount = operators.length;

        // We need to build up a list of the first column of each term. This can be
        // tricky because terms
        // can begin with a space. But we can use the operators to do this since the
        // operator is in the
        // leftmost column.
        int[] problemColumns = new int[problemCount + 1];
        String operatorLine = input.get(rowCount);
        int index = 0;
        int position = -1;
        while ((position = findNextOperatorIndex(operatorLine, position + 1)) != -1) {
            problemColumns[index] = position;
            index += 1;
        }
        // It will simplify code later on to have an extra term column
        problemColumns[index] = operatorLine.length() + 1;

        // We will still break out the data but we will keep it as a string to preserve
        // the data.
        String[][] operands = new String[problemCount][rowCount];
        for (int i = 0; i < rowCount; i++) {
            String line = input.get(i);
            for (int j = 0; j < problemCount; j++) {
                operands[j][i] = line.substring(problemColumns[j], problemColumns[j + 1] - 1);
                // System.out.print("|" + operands[j][i] + "| ");
            }
            // System.out.println();
        }

        // Now let's loop through each problem and do the math.
        long grandTotal = 0;
        for (int i = problemCount - 1; i >= 0 ; i--) {
            int columnWidth = problemColumns[i+1] - problemColumns[i] - 1;
            long[] numbers = new long[columnWidth];

            for (int j = 0; j < rowCount; j++) {
                for (int o = columnWidth - 1; o >= 0 ; o--) {
                    if (operands[i][j].substring(o, o+1).equals(" ")) continue;
                    numbers[o] = numbers[o] * 10 + Integer.parseInt(operands[i][j].substring(o, o+1));
                }
            }

            long result = 0;
            if (operators[i].equals("*")) {
                result = 1;
            }

            for (int j = 0; j < columnWidth; j++) {
                if (operators[i].equals("*")) {
                    result *= numbers[j];
                } else {
                    result += numbers[j];
                }
            }
            // System.out.println(Arrays.stream(numbers)
            // .mapToObj(String::valueOf)
            // .collect(Collectors.joining(" "+operators[i]+" ")) + " = " + result);
            grandTotal += result;
        }

        return String.valueOf(grandTotal);
    }

    public static int findNextOperatorIndex(String operatorLine, int position) {
        int starIndex = operatorLine.indexOf('*', position);
        int plusIndex = operatorLine.indexOf('+', position);
        
        // If neither is found, return -1
        if (starIndex == -1 && plusIndex == -1) {
            return -1;
        } 
        
        // If only one is found, return that one's index
        if (starIndex == -1) return plusIndex;
        if (plusIndex == -1) return starIndex;
        
        // If both are found, return the index of whichever appears sooner
        return Math.min(starIndex, plusIndex);
    }
}
