package aoc2025.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * Solution for Advent of Code 2025 Day 7: Tachyon Field Navigation
 * 
 * <p>This problem involves counting paths through a tachyon field where:
 * <ul>
 *   <li>Empty cells ('.') allow straight-through passage</li>
 *   <li>Splitter cells ('^') bifurcate the beam into two paths (left and right)</li>
 *   <li>The beam starts at 'S' and travels downward to the bottom row</li>
 * </ul>
 * 
 * <p>Part 1: Count the number of unique splitters encountered (simple counting)
 * <p>Part 2: Count total paths from start to bottom (exponential growth via dynamic programming)
 * 
 * <p>This solution demonstrates three algorithmic approaches:
 * <ol>
 *   <li><b>Iterative with Stack (Part 2):</b> Uses explicit stack with HashMap memoization</li>
 *   <li><b>Recursive with Memoization (Part 2):</b> Top-down dynamic programming</li>
 *   <li><b>Bottom-Up DP (Part 2):</b> Classic DP table approach (fastest)</li>
 * </ol>
 * 
 * <p>Performance hierarchy: Bottom-Up DP (92µs) > Memoization (525µs) > Stack (881µs)
 * 
 * @author scerruti
 * @version 1.0
 * @since 2025-12-07
 */
public class Day07 extends Day {
    /** Character constant representing an empty cell in the tachyon field */
    private static final int EMPTY = '.';
    
    /** Character constant representing the starting position */
    private static final int START = 'S';
    
    /** Character constant representing a beam splitter */
    private static final int SPLITTER = '^';

    /**
     * Constructs a Day07 solution instance.
     */
    public Day07() {
        super(7);
    }

    /**
     * Solves Part 1: Count unique splitters encountered by the beam.
     * 
     * <p>This method delegates to either the recursive or iterative implementation,
     * with optional performance benchmarking enabled by default.
     * 
     * @param input the tachyon field as lines of text
     * @return the number of unique splitters encountered
     */
    @Override
    public String part1(ArrayList<String> input) {
        boolean enableTiming = true;
        
        if (enableTiming) {
            return part1WithTiming(input);
        }
        
        // Set to true to see the naive recursive solution
        boolean useRecursive = false;
        
        if (useRecursive) {
            return part1Recursive(input);
        } else {
            return part1Iterative(input);
        }
    }

    /**
     * Benchmarks both iterative and recursive Part 1 implementations.
     * 
     * <p>Runs 1000 iterations of each approach with warmup to measure performance.
     * Demonstrates that iterative approach is ~2.2x faster than recursive due to
     * call stack overhead.
     * 
     * @param input the tachyon field as lines of text
     * @return the result from the iterative implementation
     */
    private String part1WithTiming(ArrayList<String> input) {
        int iterations = 1000;
        
        // Warmup run prevents JIT compilation from skewing results
        // Then time iterative approach
        String result1 = part1Iterative(input);
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            part1Iterative(input);
        }
        long iterativeTime = System.nanoTime() - start;
        
        // Warmup and time recursive
        String result2 = part1Recursive(input);
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            part1Recursive(input);
        }
        long recursiveTime = System.nanoTime() - start;
        
        System.out.printf("Part 1 Timing (%d iterations):%n", iterations);
        System.out.printf("  Iterative: %.3f ms (avg: %.3f µs)%n", 
            iterativeTime / 1_000_000.0, iterativeTime / (iterations * 1000.0));
        System.out.printf("  Recursive: %.3f ms (avg: %.3f µs)%n", 
            recursiveTime / 1_000_000.0, recursiveTime / (iterations * 1000.0));
        System.out.printf("  Speedup: %.2fx%n", (double)recursiveTime / iterativeTime);
        
        return result1;
    }

    /**
     * Recursive solution for Part 1: Count unique splitters.
     * 
     * <p>Uses depth-first search with two HashSets:
     * <ul>
     *   <li>visitedSplitters: tracks unique splitter positions</li>
     *   <li>visitedPositions: prevents infinite recursion from cycles</li>
     * </ul>
     * 
     * <p><b>Time Complexity:</b> O(rows × cols) in worst case
     * <p><b>Space Complexity:</b> O(rows × cols) for recursion stack and sets
     * 
     * @param input the tachyon field as lines of text
     * @return the number of unique splitters encountered
     */
    private String part1Recursive(ArrayList<String> input) {
        int tachyonFieldWidth = input.get(0).length();
        int tachyonFieldLength = input.size();
        int[][] tachyonField = new int[tachyonFieldLength][tachyonFieldWidth];
        int start = input.get(0).indexOf("S");

        // Convert input strings to 2D int array for efficient access
        for (int row = 0; row < tachyonFieldLength; row++) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                tachyonField[row][col] = input.get(row).charAt(col);
            }
        }

        // Track which splitters we've seen to avoid counting them multiple times
        HashSet<String> visitedSplitters = new HashSet<>();
        // Track all visited positions to prevent infinite recursion from cycles
        HashSet<String> visitedPositions = new HashSet<>();
        countSplitsRecursive(tachyonField, 1, start, visitedSplitters, visitedPositions);
        return String.valueOf(visitedSplitters.size());
    }

    /**
     * Recursively traverses the tachyon field to count unique splitters.
     * 
     * <p>Base cases:
     * <ul>
     *   <li>Out of bounds: return (invalid path)</li>
     *   <li>Already visited: return (prevent cycles)</li>
     * </ul>
     * 
     * <p>Recursive cases:
     * <ul>
     *   <li>Empty cell: continue straight down</li>
     *   <li>Splitter: record it, then recurse left and right</li>
     * </ul>
     * 
     * @param field the 2D tachyon field array
     * @param row current row position
     * @param col current column position
     * @param visitedSplitters set of splitter positions we've counted
     * @param visitedPositions set of all positions we've visited (prevents cycles)
     */
    private void countSplitsRecursive(int[][] field, int row, int col, 
                                     HashSet<String> visitedSplitters, 
                                     HashSet<String> visitedPositions) {
        // Base case: reached bottom or out of bounds
        if (row >= field.length || col < 0 || col >= field[0].length) {
            return;
        }

        // Check if we've already visited this position to prevent infinite loops
        // Using "row,col" format for HashMap key
        String posKey = row + "," + col;
        if (visitedPositions.contains(posKey)) {
            return; // Already explored this path
        }
        visitedPositions.add(posKey);

        // Check what's at current position and recurse accordingly
        if (field[row][col] == EMPTY) {
            // Empty cell: continue straight down to next row
            countSplitsRecursive(field, row + 1, col, visitedSplitters, visitedPositions);
        } else if (field[row][col] == SPLITTER) {
            // Record this splitter position (count it only once)
            visitedSplitters.add(posKey);
            // Recurse on both branches: beam splits left and right
            countSplitsRecursive(field, row + 1, col - 1, visitedSplitters, visitedPositions);
            countSplitsRecursive(field, row + 1, col + 1, visitedSplitters, visitedPositions);
        }
        // Note: START cell is only at top, so no need to handle it here
    }

    /**
     * Iterative solution for Part 1: Count splitters row-by-row.
     * 
     * <p>Uses level-order traversal (breadth-first) through the field:
     * <ul>
     *   <li>Tracks active beam columns at each row</li>
     *   <li>When a splitter is encountered, increment count and split beam</li>
     *   <li>Avoids duplicate columns per row using addIfUnique()</li>
     * </ul>
     * 
     * <p><b>Time Complexity:</b> O(rows × cols) worst case, typically much better
     * <p><b>Space Complexity:</b> O(cols) for beam tracking
     * <p><b>Performance:</b> ~2.2x faster than recursive due to no call stack overhead
     * 
     * @param input the tachyon field as lines of text
     * @return the number of splitters encountered
     */
    private String part1Iterative(ArrayList<String> input) {
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
        ArrayList<Integer> beamColumns; // Current row's active beam positions
        ArrayList<Integer> nextBeamColumns = new ArrayList<>(); // Next row's beam positions
        nextBeamColumns.add(start); // Initialize with starting column

        // Process each row from top to bottom (stopping before last row)
        while (row < tachyonFieldLength - 1) {
            row += 1;
            beamColumns = nextBeamColumns;
            nextBeamColumns = new ArrayList<>();

            // Process each active beam column at current row
            for (Integer column : beamColumns) {
                if (tachyonField[row][column] == EMPTY) {
                    // Empty cell: beam continues straight
                    addIfUnique(nextBeamColumns, column);
                } else if (tachyonField[row][column] == SPLITTER) {
                    // Splitter: beam splits into two beams (left and right)
                    addIfUnique(nextBeamColumns, column - 1);
                    addIfUnique(nextBeamColumns, column + 1);
                    splitCount += 1; // Count each splitter once
                }
            }
        }

        return String.valueOf(splitCount);
    }

    /**
     * Adds a column to the beam list only if it's not already present.
     * 
     * <p>This prevents counting the same column multiple times when beams
     * converge (e.g., two splitters directing beams to the same column).
     * 
     * @param nextBeamColumns list of beam columns for the next row
     * @param column the column to add
     * @return true if added, false if already present
     */
    private boolean addIfUnique(ArrayList<Integer> nextBeamColumns, Integer column) {
        if (nextBeamColumns.contains(column))
            return false; // Already tracking this column

        nextBeamColumns.add(column);
        return true;
    }

    /**
     * Solves Part 2: Count total paths from start to bottom.
     * 
     * <p>This method demonstrates three different dynamic programming approaches:
     * <ol>
     *   <li><b>Stack-based iterative with memoization:</b> Explicit stack, HashMap cache</li>
     *   <li><b>Top-down recursive with memoization:</b> Call stack, HashMap cache</li>
     *   <li><b>Bottom-up DP table:</b> Classic DP, 2D array (fastest)</li>
     * </ol>
     * 
     * <p>With timing enabled (default), all three methods are benchmarked and
     * validated to produce identical results.
     * 
     * @param input the tachyon field as lines of text
     * @return the total number of paths from start to bottom
     */
    @Override
    public String part2(ArrayList<String> input) {
        boolean enableTiming = true;
        
        if (enableTiming) {
            return part2WithTiming(input);
        }
        
        // Set to true to see the memoized recursive solution
        boolean useRecursive = false;
        
        if (useRecursive) {
            return part2Recursive(input);
        } else {
            return part2Iterative(input);
        }
    }

    /**
     * Benchmarks all three Part 2 implementations and validates consistency.
     * 
     * <p>Runs 1000 iterations of each approach:
     * <ol>
     *   <li>Stack-based iterative (typically ~881 µs)</li>
     *   <li>Top-down memoization (typically ~525 µs, 1.7x faster)</li>
     *   <li>Bottom-up DP table (typically ~92 µs, 9.6x faster)</li>
     * </ol>
     * 
     * <p>Validates that all three methods produce identical results before
     * reporting performance metrics.
     * 
     * @param input the tachyon field as lines of text
     * @return the result from the stack-based implementation
     */
    private String part2WithTiming(ArrayList<String> input) {
        int iterations = 1000;
        
        // Warmup runs prevent JIT compilation from skewing results
        // Time stack-based iterative approach
        String result1 = part2Iterative(input);
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            part2Iterative(input);
        }
        long iterativeTime = System.nanoTime() - start;
        
        // Warmup and time recursive (top-down memoization)
        String result2 = part2Recursive(input);
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            part2Recursive(input);
        }
        long recursiveTime = System.nanoTime() - start;
        
        // Warmup and time bottom-up DP
        String result3 = part2BottomUp(input);
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            part2BottomUp(input);
        }
        long bottomUpTime = System.nanoTime() - start;
        
        // Verify all three methods produce the same answer (critical validation)
        // If they don't match, there's a bug in one of the implementations
        if (!result1.equals(result2) || !result1.equals(result3)) {
            System.err.println("ERROR: Solutions don't match!");
            System.err.println("  Stack-based: " + result1);
            System.err.println("  Memoization: " + result2);
            System.err.println("  Bottom-Up:   " + result3);
            return "ERROR: Inconsistent results";
        }
        
        System.out.printf("Part 2 Timing (%d iterations):%n", iterations);
        System.out.printf("  Iterative (Stack): %.3f ms (avg: %.3f µs)%n", 
            iterativeTime / 1_000_000.0, iterativeTime / (iterations * 1000.0));
        System.out.printf("  Recursive (Memo):  %.3f ms (avg: %.3f µs)%n", 
            recursiveTime / 1_000_000.0, recursiveTime / (iterations * 1000.0));
        System.out.printf("  Bottom-Up DP:      %.3f ms (avg: %.3f µs)%n", 
            bottomUpTime / 1_000_000.0, bottomUpTime / (iterations * 1000.0));
        System.out.printf("  Speedup (Stack vs Memo): %.2fx%n", (double)iterativeTime / recursiveTime);
        System.out.printf("  Speedup (Stack vs DP):   %.2fx%n", (double)iterativeTime / bottomUpTime);
        System.out.printf("  Speedup (Memo vs DP):    %.2fx%n", (double)recursiveTime / bottomUpTime);
        System.out.printf("  All methods agree: %s%n", result1);
        
        return result1;
    }

    /**
     * Top-down recursive solution with memoization for Part 2.
     * 
     * <p>Uses a HashMap to cache results for each (row, col) position:
     * <ul>
     *   <li>Key format: "row,col" as String</li>
     *   <li>Value: number of paths from this position to bottom</li>
     * </ul>
     * 
     * <p><b>Time Complexity:</b> O(rows × cols) with memoization
     * <p><b>Space Complexity:</b> O(rows × cols) for cache + O(rows) recursion depth
     * <p><b>Performance:</b> Middle ground (~525 µs), benefits from memoization
     * but suffers from HashMap hashing overhead and call stack depth
     * 
     * @param input the tachyon field as lines of text
     * @return the total number of paths from start to bottom
     */
    private String part2Recursive(ArrayList<String> input) {
        int tachyonFieldWidth = input.get(0).length();
        int tachyonFieldLength = input.size();
        int[][] tachyonField = new int[tachyonFieldLength][tachyonFieldWidth];
        int start = input.get(0).indexOf("S");

        for (int row = 0; row < tachyonFieldLength; row++) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                tachyonField[row][col] = input.get(row).charAt(col);
            }
        }

        HashMap<String, Long> cache = new HashMap<>();
        long result = countPathsRecursive(tachyonField, 0, start, cache);
        return String.valueOf(result);
    }

    /**
     * Recursively counts paths from (row, col) to bottom with memoization.
     * 
     * <p>Base cases:
     * <ul>
     *   <li>Out of bounds: 0 paths</li>
     *   <li>At bottom row: 1 path (we've arrived)</li>
     *   <li>Already computed: return cached value</li>
     * </ul>
     * 
     * <p>Recursive case: look at cell below and sum paths through it
     * 
     * @param field the 2D tachyon field array
     * @param row current row position
     * @param col current column position
     * @param cache memoization cache mapping "row,col" -> path count
     * @return number of paths from this position to bottom row
     */
    private long countPathsRecursive(int[][] field, int row, int col, HashMap<String, Long> cache) {
        // Base case: out of bounds (invalid path)
        if (row >= field.length || col < 0 || col >= field[0].length) {
            return 0;
        }
        
        // Base case: reached bottom row (successful path)
        if (row == field.length - 1) {
            return 1;
        }

        // Check cache for previously computed result
        String key = row + "," + col;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        long paths = 0;
        
        // Check what's at next row and recurse accordingly
        if (field[row + 1][col] == EMPTY) {
            // Empty cell: continue straight down
            paths = countPathsRecursive(field, row + 1, col, cache);
        } else if (field[row + 1][col] == SPLITTER) {
            // Splitter: sum paths through both branches (left and right)
            paths = countPathsRecursive(field, row + 1, col - 1, cache) +
                    countPathsRecursive(field, row + 1, col + 1, cache);
        }

        // Store result in cache before returning
        cache.put(key, paths);
        return paths;
    }

    /**
     * Stack-based iterative solution with memoization for Part 2.
     * 
     * <p>Uses an explicit stack (ArrayList) to simulate recursion:
     * <ul>
     *   <li>Push positions onto stack when descending</li>
     *   <li>Pop and compute when all children are cached</li>
     *   <li>HashMap stores results for each Point</li>
     * </ul>
     * 
     * <p><b>Time Complexity:</b> O(rows × cols) with memoization
     * <p><b>Space Complexity:</b> O(rows × cols) for cache + stack
     * <p><b>Performance:</b> Slowest (~881 µs) due to HashMap overhead,
     * object allocation for Point, and stack management
     * 
     * @param input the tachyon field as lines of text
     * @return the total number of paths from start to bottom
     */
    private String part2Iterative(ArrayList<String> input) {
        int tachyonFieldWidth = input.get(0).length();
        int tachyonFieldLength = input.size();
        int[][] tachyonField = new int[tachyonFieldLength][tachyonFieldWidth];
        int start = input.get(0).indexOf("S");

        for (int row = 0; row < tachyonFieldLength; row++) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                tachyonField[row][col] = input.get(row).charAt(col);
            }
        }

        // Stack of positions to process (last element is top of stack)
        ArrayList<Point> particleLocations = new ArrayList<>();
        // Cache mapping Point -> number of paths below it
        HashMap<Point, Long> pathsBelow = new HashMap<>();
        particleLocations.add(new Point(0, start)); // Push starting position

        // Process stack until empty (all positions resolved)
        while (particleLocations.size() > 0) {
            int lastIndex = particleLocations.size() - 1;
            Point current = particleLocations.get(lastIndex); // Peek at top
            // Pre-compute neighbor Points (used for HashMap lookups)
            Point next = new Point(current.row + 1, current.column);
            Point right = new Point(current.row + 1, current.column + 1);
            Point left = new Point(current.row + 1, current.column - 1);

            int nextRow = current.row + 1;
            // Base case: reached bottom row
            if (nextRow >= tachyonFieldLength) {
                pathsBelow.put(current, 1L); // 1 path (we're at bottom)
                particleLocations.remove(lastIndex); // Pop from stack
                continue;
            }

            // Check cell below current position
            if (tachyonField[nextRow][current.column] == EMPTY) {
                // Empty cell: paths through current = paths through cell below
                if (pathsBelow.containsKey(next)) {
                    // Cell below already computed, use cached value
                    pathsBelow.put(current, pathsBelow.get(next));
                    particleLocations.remove(lastIndex); // Pop from stack
                } else {
                    // Need to compute cell below first, push it onto stack
                    particleLocations.add(next);
                }
            } else if (tachyonField[nextRow][current.column] == SPLITTER) {
                // Splitter: paths through current = sum of paths through left + right
                boolean descended = false; // Track if we pushed any children onto stack
                long lowerPaths = 0;
                // Check right branch
                if (pathsBelow.containsKey(right)) {
                    lowerPaths = pathsBelow.get(right); // Use cached value
                } else {
                    particleLocations.add(right); // Need to compute, push onto stack
                    descended = true;
                }
                // Check left branch
                if (pathsBelow.containsKey(left)) {
                    lowerPaths += pathsBelow.get(left); // Use cached value
                } else {
                    particleLocations.add(left); // Need to compute, push onto stack
                    descended = true;
                }
                // If both children cached, compute current and pop from stack
                if (!descended) {
                    pathsBelow.put(current, lowerPaths);
                    particleLocations.remove(lastIndex); // Pop from stack
                }
                // Otherwise, children will be processed in next iterations
            }
        }

        return String.valueOf(pathsBelow.get(new Point(0, start)));
    }

    /**
     * Bottom-up dynamic programming solution for Part 2 (FASTEST).
     * 
     * <p>Classic DP approach using 2D array:
     * <ul>
     *   <li>dp[row][col] = number of paths from (row, col) to bottom</li>
     *   <li>Base case: bottom row has 1 path from each position</li>
     *   <li>Recurrence: work backward from second-to-last row to top</li>
     * </ul>
     * 
     * <p><b>Time Complexity:</b> O(rows × cols)
     * <p><b>Space Complexity:</b> O(rows × cols) for DP table
     * <p><b>Performance:</b> Fastest (~92 µs) due to:
     * <ul>
     *   <li>Direct array access (no hashing)</li>
     *   <li>Cache-friendly sequential memory access</li>
     *   <li>No function call overhead</li>
     *   <li>Primitive long array (no object allocation)</li>
     * </ul>
     * 
     * @param input the tachyon field as lines of text
     * @return the total number of paths from start to bottom
     */
    private String part2BottomUp(ArrayList<String> input) {
        int tachyonFieldWidth = input.get(0).length();
        int tachyonFieldLength = input.size();
        int[][] tachyonField = new int[tachyonFieldLength][tachyonFieldWidth];
        int start = input.get(0).indexOf("S");

        for (int row = 0; row < tachyonFieldLength; row++) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                tachyonField[row][col] = input.get(row).charAt(col);
            }
        }

        // Create DP table: dp[row][col] = number of paths from (row,col) to bottom
        long[][] dp = new long[tachyonFieldLength][tachyonFieldWidth];
        
        // Base case: bottom row - all positions have 1 path (already at destination)
        for (int col = 0; col < tachyonFieldWidth; col++) {
            dp[tachyonFieldLength - 1][col] = 1;
        }
        
        // Fill table bottom-up (from second-to-last row upward to row 0)
        // This ensures when we compute dp[row][col], we already have dp[row+1][*]
        for (int row = tachyonFieldLength - 2; row >= 0; row--) {
            for (int col = 0; col < tachyonFieldWidth; col++) {
                // Check what's at the next row to determine recurrence relation
                int nextRow = row + 1;
                if (tachyonField[nextRow][col] == EMPTY) {
                    // Empty cell: paths from (row,col) = paths from (row+1,col)
                    dp[row][col] = dp[nextRow][col];
                } else if (tachyonField[nextRow][col] == SPLITTER) {
                    // Splitter: paths from (row,col) = paths from left + paths from right
                    long leftPaths = 0;
                    long rightPaths = 0;
                    
                    // Check left branch (bounds check)
                    if (col - 1 >= 0) {
                        leftPaths = dp[nextRow][col - 1];
                    }
                    // Check right branch (bounds check)
                    if (col + 1 < tachyonFieldWidth) {
                        rightPaths = dp[nextRow][col + 1];
                    }
                    
                    // Sum paths from both branches
                    dp[row][col] = leftPaths + rightPaths;
                }
                // Note: if cell below is START or other, dp[row][col] remains 0 (no paths)
            }
        }
        
        // Answer is stored at starting position
        return String.valueOf(dp[0][start]);
    }

    /**
     * Inner class representing a 2D coordinate in the tachyon field.
     * 
     * <p>Used as a HashMap key in the stack-based iterative solution.
     * Implements equals() and hashCode() for proper HashMap behavior.
     * 
     * <p><b>Note:</b> Inner classes are <b>not in the AP Computer Science A subset</b>.
     * In production code, Point would typically be a separate top-level class.
     * Keeping it as an inner class here serves two pedagogical purposes:
     * <ol>
     *   <li>Keeps all related code in one file for easier demonstration</li>
     *   <li>Introduces students to inner classes as a technique used in college
     *       CS courses and professional development</li>
     * </ol>
     */
    public class Point {
        /** Row coordinate in the tachyon field */
        int row;
        /** Column coordinate in the tachyon field */
        int column;

        /**
         * Constructs a Point at the specified coordinates.
         * 
         * @param row the row coordinate
         * @param column the column coordinate
         */
        public Point(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public String toString() {
            return "Point [row=" + row + ", column=" + column + "]";
        }

        /**
         * Compares this Point to another object for equality.
         * Two Points are equal if they have the same row and column.
         * 
         * @param o the object to compare
         * @return true if the objects are equal, false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true; // Same object reference
            if (o == null || getClass() != o.getClass())
                return false; // Null or different class
            Point point = (Point) o;

            // Points are equal if coordinates match
            return row == point.row && column == point.column;
        }

        /**
         * Computes hash code for this Point.
         * Required for proper HashMap behavior alongside equals().
         * 
         * @return hash code based on row and column
         */
        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
