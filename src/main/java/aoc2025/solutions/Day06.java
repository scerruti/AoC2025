package aoc2025.solutions;

import java.util.ArrayList;
import java.util.Scanner;
// import java.util.Arrays;
// import java.util.stream.Collectors;

/**
 * Day 6: Trash Compactor - Cephalopod Math Worksheet
 * 
 * Solves math problems from a worksheet where problems are arranged vertically in columns.
 * Part 1: Numbers read left-to-right (normal reading order)
 * Part 2: Numbers read right-to-left within each column (cephalopod reading order)
 */
public class Day06 extends Day {
    private static final String MULTIPLY = "*";
    private static final String ADD = "+";
    
    public Day06() {
        super(6);
    }

    /**
     * Part 1: Solve math problems arranged in columns (left-to-right reading)
     * 
     * Each problem consists of numbers stacked vertically, with an operator at the bottom.
     * Problems are separated by spaces and read in normal left-to-right order.
     * 
     * Example:
     *   123 328
     *    45  64
     *     6  98
     *    *   +
     * 
     * Represents: (123 * 45 * 6) + (328 + 64 + 98)
     * 
     * @param input List of strings representing the worksheet rows
     * @return The grand total (sum of all problem results)
     */
    @Override
    public String part1(ArrayList<String> input) {
        int rowCount = input.size() - 1;
        
        // Count problems by scanning first line for numbers
        // Scanner automatically handles multiple spaces between numbers
        Scanner firstLineScanner = new Scanner(input.get(0));
        int problemCount = 0;
        while (firstLineScanner.hasNextLong()) {
            firstLineScanner.nextLong();
            problemCount++;
        }
        firstLineScanner.close();

        // Store problems in 2D array: problems[problemIndex][rowIndex]
        // Each column in the array represents one problem's numbers
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
            // Initialize result with identity value: 1 for multiplication, 0 for addition
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

    /**
     * Part 2: Solve math problems with right-to-left column reading (cephalopod style)
     * 
     * Numbers are read right-to-left within each column, with the most significant digit
     * at the top. Spaces within columns are significant and indicate digit positions.
     * 
     * Example:
     * <pre>
     * 123 328
     *  45  64
     *   6  98
     * *   +
     * </pre>
     * 
     * In Part 2, each problem is read vertically within its column space, with digits
     * assembled right-to-left. The actual algorithm is complex and demonstrates advanced
     * string manipulation with position tracking. See the code implementation for details.
     * 
     * This is significantly more complex than Part 1 and is beyond typical AP CSA scope.
     * 
     * @param input List of strings representing the worksheet rows
     * @return The grand total (sum of all problem results)
     */
    @Override
    public String part2(ArrayList<String> input) {
        int rowCount = input.size() - 1;

        // Part 2: Spaces are significant for positioning
        // Use the operator line to determine problem positions
        String[] operators = input.get(rowCount).replaceAll("\\s+", " ").trim().split(" ");
        int problemCount = operators.length;

        // Find the column position of each operator to determine problem boundaries
        // Each operator marks the leftmost column of its problem
        int[] problemColumns = new int[problemCount + 1];
        String operatorLine = input.get(rowCount);
        int index = 0;
        int position = -1;
        while ((position = findNextOperatorIndex(operatorLine, position + 1)) != -1) {
            problemColumns[index] = position;
            index += 1;
        }
        // Extra entry simplifies boundary calculations later
        problemColumns[index] = operatorLine.length() + 1;

        // Extract each problem's column as a string to preserve spacing
        // Spaces within a column indicate empty digit positions
        String[][] operands = new String[problemCount][rowCount];
        for (int row = 0; row < rowCount; row++) {
            String line = input.get(row);
            for (int problemIndex = 0; problemIndex < problemCount; problemIndex++) {
                operands[problemIndex][row] = line.substring(problemColumns[problemIndex], problemColumns[problemIndex + 1] - 1);
                // System.out.print("|" + operands[problemIndex][row] + "| ");
            }
            // System.out.println();
        }

        // Process each problem from right to left
        long grandTotal = 0;
        for (int problemIndex = problemCount - 1; problemIndex >= 0 ; problemIndex--) {
            int columnWidth = problemColumns[problemIndex+1] - problemColumns[problemIndex] - 1;
            long[] numbers = new long[columnWidth];

            // Build numbers by reading digits right-to-left, top-to-bottom
            // Each digit position contributes to building up the number
            for (int row = 0; row < rowCount; row++) {
                for (int digitPos = columnWidth - 1; digitPos >= 0 ; digitPos--) {
                    if (operands[problemIndex][row].charAt(digitPos) == ' ') continue;
                    numbers[digitPos] = numbers[digitPos] * 10 + Integer.parseInt(operands[problemIndex][row].substring(digitPos, digitPos+1));
                }
            }

            // Initialize result with identity value: 1 for multiplication, 0 for addition
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

    /**
     * Helper method to find the next operator position in a line.
     * 
     * Searches for either '*' or '+' starting from the given position.
     * 
     * @param operatorLine The line containing operators
     * @param position The position to start searching from
     * @return The index of the next operator, or -1 if none found
     */
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
