package aoc2025.solutions;

import java.util.ArrayList;
import java.util.Scanner;
// import java.util.Arrays;
// import java.util.stream.Collectors;

public class Day06 extends Day {
    private static final String MULTIPLY = "*";
    private static final String ADD = "+";
    
    public Day06() {
        super(6);
    }

    @Override
    public String part1(ArrayList<String> input) {
        int rowCount = input.size() - 1;
        
        // Use Scanner to count problems in first line
        Scanner firstLineScanner = new Scanner(input.get(0));
        int problemCount = 0;
        while (firstLineScanner.hasNextLong()) {
            firstLineScanner.nextLong();
            problemCount++;
        }
        firstLineScanner.close();

        long[][] problems = new long[problemCount][rowCount];
        for (int row = 0; row < rowCount; row++) {
            // Use Scanner to read numbers, automatically handling multiple spaces
            Scanner lineScanner = new Scanner(input.get(row));
            int problemIndex = 0;
            while (lineScanner.hasNextLong()) {
                problems[problemIndex][row] = lineScanner.nextLong();
                problemIndex++;
            }
            lineScanner.close();
        }
        
        // Use Scanner to read operators
        Scanner operatorScanner = new Scanner(input.get(rowCount));
        String[] operators = new String[problemCount];
        int operatorIndex = 0;
        while (operatorScanner.hasNext()) {
            operators[operatorIndex] = operatorScanner.next();
            operatorIndex++;
        }
        operatorScanner.close();

        long grandTotal = 0;
        for (int problemIndex = 0; problemIndex < problemCount; problemIndex++) {
            long result = operators[problemIndex].equals(MULTIPLY) ? 1 : 0;

            for (int numberIndex = 0; numberIndex < rowCount; numberIndex++) {
                if (operators[problemIndex].equals(MULTIPLY)) {
                    result *= problems[problemIndex][numberIndex];
                } else {
                    result += problems[problemIndex][numberIndex];
                }
            }
            // System.out.println(Arrays.stream(problems[problemIndex])
            // .mapToObj(String::valueOf)
            // .collect(Collectors.joining(" "+operators[problemIndex]+" ")) + " = " + result);
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
        for (int row = 0; row < rowCount; row++) {
            String line = input.get(row);
            for (int problemIndex = 0; problemIndex < problemCount; problemIndex++) {
                operands[problemIndex][row] = line.substring(problemColumns[problemIndex], problemColumns[problemIndex + 1] - 1);
                // System.out.print("|" + operands[problemIndex][row] + "| ");
            }
            // System.out.println();
        }

        // Now let's loop through each problem and do the math.
        long grandTotal = 0;
        for (int problemIndex = problemCount - 1; problemIndex >= 0 ; problemIndex--) {
            int columnWidth = problemColumns[problemIndex+1] - problemColumns[problemIndex] - 1;
            long[] numbers = new long[columnWidth];

            for (int row = 0; row < rowCount; row++) {
                for (int digitPos = columnWidth - 1; digitPos >= 0 ; digitPos--) {
                    if (operands[problemIndex][row].charAt(digitPos) == ' ') continue;
                    numbers[digitPos] = numbers[digitPos] * 10 + Integer.parseInt(operands[problemIndex][row].substring(digitPos, digitPos+1));
                }
            }

            long result = operators[problemIndex].equals(MULTIPLY) ? 1 : 0;

            for (int numberIndex = 0; numberIndex < columnWidth; numberIndex++) {
                if (operators[problemIndex].equals(MULTIPLY)) {
                    result *= numbers[numberIndex];
                } else {
                    result += numbers[numberIndex];
                }
            }
            // System.out.println(Arrays.stream(numbers)
            // .mapToObj(String::valueOf)
            // .collect(Collectors.joining(" "+operators[problemIndex]+" ")) + " = " + result);
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
