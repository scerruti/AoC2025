package aoc2025.solutions;

// import java.util.Arrays;
import java.util.ArrayList;
// import java.util.stream.Collectors;


public class Day06 extends Day {
    public Day06() { super(6); }

    @Override
    public String part1(ArrayList<String> input) {
        int numberOfOperands = input.size() - 1;
        int numberOfProblems = input.get(0).replaceAll("\\s+", " ").trim().split(" ").length;

        long[][] operands = new long[numberOfProblems][numberOfOperands];
        for (int i = 0; i < numberOfOperands; i++) {
            // System.out.print(input.get(i) + ": ");
            // I fell into a trap here if trying to split by " " but there are multiple spaces
            // I could have avoided the regular expression by reading the data with Scanner
            // I also did not observe I needed to trim the string before processing to remove leading spaces
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
            //                         .mapToObj(String::valueOf)
            //                         .collect(Collectors.joining(" "+operators[i]+" ")) + " = " + result); 
            sum += result;
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2(ArrayList<String> input) {
        // TODO: Implement Part 2
        return "";
    }
}
