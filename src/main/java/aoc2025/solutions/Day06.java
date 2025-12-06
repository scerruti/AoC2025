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
        int numberOfOperands = input.size() - 1;
        int numberOfProblems = input.get(0).replaceAll("\\s+", " ").trim().split(" ").length;

        long[][] operands = new long[numberOfProblems][numberOfOperands];
        for (int i = 0; i < numberOfOperands; i++) {
            // System.out.print(input.get(i) + ": ");
            // I fell into a trap here if trying to split by " " but there are multiple
            // spaces
            // I could have avoided the regular expression by reading the data with Scanner
            // I also did not observe I needed to trim the string before processing to
            // remove leading spaces
            String normalizedLine = input.get(i).replaceAll("\\s+", " ").trim();
            // System.out.print(normalizedLine + ": ");
            // System.out.print("[" + operands.length + "] ");
            String[] problemsOperandRow = normalizedLine.split(" ");
            for (int j = 0; j < numberOfProblems; j++) {
                operands[j][i] = Long.parseLong(problemsOperandRow[j]);
                // System.out.print("|" + operands[j][i] + "| ");
            }
            // System.out.println();
        }
        String[] operators = input.get(numberOfOperands).replaceAll("\\s+", " ").trim().split(" ");
        // System.out.println(Arrays.toString(operators));

        long sum = 0;
        for (int i = 0; i < numberOfProblems; i++) {
            long result = 0;
            if (operators[i].equals("*")) {
                result = 1;
            }

            for (int j = 0; j < numberOfOperands; j++) {
                if (operators[i].equals("*")) {
                    result *= operands[i][j];
                } else {
                    result += operands[i][j];
                }
            }
            // System.out.println(Arrays.stream(operands[i])
            // .mapToObj(String::valueOf)
            // .collect(Collectors.joining(" "+operators[i]+" ")) + " = " + result);
            sum += result;
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2(ArrayList<String> input) {
        int numberOfDigits = input.size() - 1;

        // Spaces are now important. It seems like we need to determine the number of
        // columns each row
        // uses to represent a number. We'll use the operands row to determine the
        // number of problems for safety.
        String[] operators = input.get(numberOfDigits).replaceAll("\\s+", " ").trim().split(" ");
        int numberOfProblems = operators.length;

        // We need to build up a list of the first column of each term. This can be
        // tricky because terms
        // can begin with a space. But we can use the operators to do this since the
        // operator is in the
        // leftmost column.
        int[] termColumns = new int[numberOfProblems + 1];
        String operatorLine = input.get(numberOfDigits);
        int index = 0;
        int position = -1;
        while ((position = indexOfOperator(operatorLine, position + 1)) != -1) {
            termColumns[index] = position;
            index += 1;
        }
        // It will simplify code later on to have an extra term column
        termColumns[index] = operatorLine.length() + 1;

        // We will still break out the data but we will keep it as a string to preserve
        // the data.
        String[][] operands = new String[numberOfProblems][numberOfDigits];
        for (int i = 0; i < numberOfDigits; i++) {
            String line = input.get(i);
            for (int j = 0; j < numberOfProblems; j++) {
                operands[j][i] = line.substring(termColumns[j], termColumns[j + 1] - 1);
                // System.out.print("|" + operands[j][i] + "| ");
            }
            // System.out.println();
        }

        // Now let's loop through each problem and do the math.
        long sum = 0;
        for (int i = numberOfProblems - 1; i >= 0 ; i--) {
            int numberOfOperands = termColumns[i+1] - termColumns[i] - 1;
            long[] humanOps = new long[numberOfOperands];

            for (int j = 0; j < numberOfDigits; j++) {
                for (int o = numberOfOperands - 1; o >= 0 ; o--) {
                    if (operands[i][j].substring(o, o+1).equals(" ")) continue;
                    humanOps[o] = humanOps[o] * 10 + Integer.parseInt(operands[i][j].substring(o, o+1));
                }
            }

            long result = 0;
            if (operators[i].equals("*")) {
                result = 1;
            }

            for (int j = 0; j < numberOfOperands; j++) {
                if (operators[i].equals("*")) {
                    result *= humanOps[j];
                } else {
                    result += humanOps[j];
                }
            }
            // System.out.println(Arrays.stream(humanOps)
            // .mapToObj(String::valueOf)
            // .collect(Collectors.joining(" "+operators[i]+" ")) + " = " + result);
            sum += result;
        }

        return String.valueOf(sum);
    }

    public static int indexOfOperator(String operatorLine, int position) {
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
