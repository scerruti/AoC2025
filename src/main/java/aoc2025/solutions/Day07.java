package aoc2025.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Day07 extends Day {
    private static final int EMPTY = '.';
    private static final int START = 'S';
    private static final int SPLITTER = '^';

    public Day07() {
        super(7);
    }

    @Override
    public String part1(ArrayList<String> input) {
        int tachyonFieldWidth = input.get(0).length();
        int tachyonFieldLength = input.size();
        int[][] tachyonField = new int[tachyonFieldLength][tachyonFieldWidth];
        int start = input.get(0).indexOf("S");

        for (int row = 0; row < tachyonFieldLength; row++) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                tachyonField[row][col] = input.get(row).charAt(col);
            }
        }

        int row = 1;
        int splitCount = 0;
        ArrayList<Integer> beamColumns;
        ArrayList<Integer> nextBeamColumns = new ArrayList<>();
        nextBeamColumns.add(new Integer(start));

        while (row < tachyonFieldLength - 1) {
            row += 1;
            beamColumns = nextBeamColumns;
            nextBeamColumns = new ArrayList<>();

            for (Integer column : beamColumns) {
                if (tachyonField[row][column] == EMPTY) {
                    addIfUnique(nextBeamColumns, column);
                } else if (tachyonField[row][column] == SPLITTER) {
                    addIfUnique(nextBeamColumns, column - 1);
                    addIfUnique(nextBeamColumns, column + 1);
                    splitCount += 1;
                }
            }
        }

        return String.valueOf(splitCount);
    }

    private boolean addIfUnique(ArrayList<Integer> nextBeamColumns, Integer column) {
        if (nextBeamColumns.contains(column))
            return false;

        nextBeamColumns.add(column);
        return true;
    }

    @Override
    public String part2(ArrayList<String> input) {
        int tachyonFieldWidth = input.get(0).length();
        int tachyonFieldLength = input.size();
        int[][] tachyonField = new int[tachyonFieldLength][tachyonFieldWidth];
        int start = input.get(0).indexOf("S");

        for (int row = 0; row < tachyonFieldLength; row++) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                tachyonField[row][col] = input.get(row).charAt(col);
            }
        }

        ArrayList<Point> particleLocations = new ArrayList<>();
        HashMap<Point, Long> pathsBelow = new HashMap<>();
        particleLocations.add(new Point(0, start));

        while (particleLocations.size() > 0) {
            Point current = particleLocations.get(0);
            // System.out.println(current);
            Point next = new Point(current.row + 1, current.column);
            Point right = new Point(current.row + 1, current.column + 1);
            Point left = new Point(current.row + 1, current.column - 1);

            int nextRow = current.row + 1;
            if (nextRow >= tachyonFieldLength) {
                pathsBelow.put(current, 1L);
                particleLocations.remove(0);
                continue;
            }

            if (tachyonField[nextRow][current.column] == EMPTY) {
                if (pathsBelow.keySet().contains(next)) {
                    pathsBelow.put(current, pathsBelow.get(next));
                    // System.out.println("Cached: " + current + " " + pathsBelow.get(current));
                    particleLocations.remove(0);
                } else {
                    particleLocations.add(0, next);
                }
            } else if (tachyonField[nextRow][current.column] == SPLITTER) {
                boolean descended = false;
                long lowerPaths = 0;
                if (pathsBelow.keySet().contains(right)) {
                    lowerPaths = pathsBelow.get(right);
                } else {
                    particleLocations.add(0, right);
                    descended = true;
                }
                if (pathsBelow.keySet().contains(left)) {
                    lowerPaths += pathsBelow.get(left);
                } else {
                    particleLocations.add(0, left);
                    descended = true;
                }
                if (!descended) {
                    pathsBelow.put(current, lowerPaths);
                    // System.out.println("Cached: " + current + " " + pathsBelow.get(current));
                    particleLocations.remove(0);
                }
            }
        }

        return String.valueOf(pathsBelow.get(new Point(0, start)));
    }

    public class Point {
        Integer row;
        Integer column;

        public Point(Integer row, Integer column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public String toString() {
            return "Point [row=" + row + ", column=" + column + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false; // Or use instanceof
            Point point = (Point) o;

            return Objects.equals(row, point.row) && Objects.equals(column, point.column);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
