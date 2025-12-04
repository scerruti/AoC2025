package aoc2025.solutions;

import java.util.ArrayList;


public class Day04 extends Day {
    public Day04() { super(4); }

    @Override
    public String part1(ArrayList<String> input) {
        int count = 0;
        int width = input.get(0).length();

        int[][] warehouse = getRowNumber(input, width);

        for (int rowNumber = 1; rowNumber <= width; rowNumber++) {
            for (int colNumber = 1; colNumber <= width; colNumber++) {
                if (warehouse[rowNumber][colNumber] == 1 && numberNeighbors(warehouse, rowNumber, colNumber) < 4) {
                    count += 1;
                }
            }
        }
        return String.valueOf(count);
    }

    private int[][] getRowNumber(ArrayList<String> input, int width) {
        int[][] warehouse = new int[width + 2][width + 2];
        int rowNumber = 1;
        for (String row : input) {
            for (int colNumber = 1; colNumber <= width; colNumber++) {
                if (row.substring(colNumber-1, colNumber).equals("@")) {
                    warehouse[rowNumber][colNumber] = 1;
                }
            }
            rowNumber += 1;
        }

        return warehouse;
    }

    private int numberNeighbors(int[][] warehouse, int rowNumber, int colNumber) {
        return warehouse[rowNumber-1][colNumber-1] + warehouse[rowNumber-1][colNumber] + warehouse[rowNumber-1][colNumber+1] +
               warehouse[rowNumber][colNumber-1] + warehouse[rowNumber][colNumber+1] +
               warehouse[rowNumber+1][colNumber-1] + warehouse[rowNumber+1][colNumber] + warehouse[rowNumber+1][colNumber+1];
    }

    @Override
    public String part2(ArrayList<String> input) {
        int count = 0;
        int width = input.get(0).length();

        int[][] warehouse = getRowNumber(input, width);

        boolean rollsRemoved = true;
        while (rollsRemoved) {
            rollsRemoved = false;
            for (int rowNumber = 1; rowNumber <= width; rowNumber++) {
                for (int colNumber = 1; colNumber <= width; colNumber++) {
                    if (warehouse[rowNumber][colNumber] == 1 && numberNeighbors(warehouse, rowNumber, colNumber) < 4) {
                        count += 1;
                        warehouse[rowNumber][colNumber] = 2;
                    }
                }
            }
            for (int rowNumber = 1; rowNumber <= width; rowNumber++) {
                for (int colNumber = 1; colNumber <= width; colNumber++) {
                    if (warehouse[rowNumber][colNumber] == 2) {
                        warehouse[rowNumber][colNumber] = 0;
                        rollsRemoved = true;
                    }
                }
            }
        }
        return String.valueOf(count);
    }
}
