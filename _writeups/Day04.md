---
layout: post
title: "Day 4: Printing Department"
date: 2025-12-04
categories: [writeup]
tags: [AP-CSA, LO-1.3.C, LO-2.3.A, LO-2.7.B, LO-2.8.A, LO-4.1.A, LO-4.2.A, LO-4.9.A, 2D-arrays, simulation, cellular-automaton, debugging]
---

# Day 4: Printing Department

> **Note:** This writeup was generated with assistance from GitHub Copilot (Claude Sonnet 4.5) to document the solution and pedagogical approach.

## Problem Summary

Day 4 takes place in the printing department, where large rolls of paper (`@`) are arranged on a grid with empty spaces (`.`). The challenge involves optimizing forklift access to these paper rolls:

- **Part 1:** Count all rolls that forklifts can access. A forklift can only access a roll if there are fewer than 4 neighboring rolls (using 8-directional neighbors: horizontal, vertical, and diagonal).
- **Part 2:** Simulate a cascading removal process where accessible rolls are removed by forklifts, potentially making other rolls accessible. Count the total number of rolls removed.

This is a cellular automaton simulation where the state of each cell depends on its neighbors.

## AP CSA Subset Compliance

This solution is fully AP CSA subset compliant:

- All constructs (2D arrays, loops, conditionals, `String` methods, `ArrayList`) are AP CSA compliant.
- No use of `long`, `charAt`, streams, or other non-subset features.

## Solution Overview

### Part 1: Counting Vulnerable Rolls

The approach uses a 2D array to represent the warehouse, with padding around the edges to avoid boundary checks:

1. Parse the input into a 2D array with dimensions $(n + 2) \times (n + 2)$, leaving a border of zeros.
2. Mark each roll position with `1`, empty spaces with `0`.
3. For each roll, count its 8 neighbors using the `numberNeighbors` method.
4. If a roll has fewer than 4 neighbors, increment the count.

**Key insight:** Using a padded array (border of zeros) eliminates the need for boundary checking when counting neighbors.

```java
public String part1(ArrayList<String> input) {
    int count = 0;
    int width = input.get(0).length();
    int[][] warehouse = getRowNumber(input, width);

    for (int rowNumber = 1; rowNumber <= width; rowNumber++) {
        for (int colNumber = 1; colNumber <= width; colNumber++) {
            if (warehouse[rowNumber][colNumber] == 1 && 
                numberNeighbors(warehouse, rowNumber, colNumber) < 4) {
                count += 1;
            }
        }
    }
    return String.valueOf(count);
}
```

### Part 2: Cascading Removal Simulation

Part 2 requires iteratively removing rolls until no more can be removed:

1. Identify all rolls with fewer than 4 neighbors and mark them for removal (set to `2`).
2. Remove marked rolls (set to `0`) in a second pass.
3. Repeat until no rolls are removed in a complete pass.

**Critical implementation detail:** The two-pass approach is essential—you cannot remove rolls immediately during neighbor counting because doing so would corrupt the neighbor counts for subsequent cells in the same iteration.

```java
public String part2(ArrayList<String> input) {
    int count = 0;
    int width = input.get(0).length();
    int[][] warehouse = getRowNumber(input, width);

    boolean rollsRemoved = true;
    while (rollsRemoved) {
        rollsRemoved = false;
        // First pass: Mark rolls for removal
        for (int rowNumber = 1; rowNumber <= width; rowNumber++) {
            for (int colNumber = 1; colNumber <= width; colNumber++) {
                if (warehouse[rowNumber][colNumber] == 1 && 
                    numberNeighbors(warehouse, rowNumber, colNumber) < 4) {
                    count += 1;
                    warehouse[rowNumber][colNumber] = 2;
                }
            }
        }
        // Second pass: Remove marked rolls
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
```

## Common Pitfalls and Debugging

### Error 1: Counting Empty Spaces Instead of Rolls

**The Problem:** Initially, the code counted *all* positions with fewer than 4 neighbors, including empty spaces. This led to incorrect results because empty spaces shouldn't be counted at all.

**The Fix:** Add a condition to check that the current position contains a roll (`warehouse[rowNumber][colNumber] == 1`) before checking neighbor count:

```java
if (warehouse[rowNumber][colNumber] == 1 && 
    numberNeighbors(warehouse, rowNumber, colNumber) < 4) {
    count += 1;
}
```

**Teaching Point:** Always verify that your loop conditions match the problem requirements. When iterating over a grid, it's easy to process all cells when you should only process specific ones.

### Error 2: Single-Pass Removal in Part 2

**The Problem:** An initial attempt tried to remove rolls immediately upon finding them (single pass), but this corrupted neighbor counts for cells processed later in the same iteration. When you remove a roll while iterating, subsequent cells see an incorrect number of neighbors.

**Why It Fails:** Consider a row of rolls like `@@@`. If you remove the first roll immediately, the second roll now sees only 1 neighbor instead of 2, causing incorrect cascading behavior.

**The Solution:** Use a two-pass approach:
1. **Mark phase:** Identify all rolls to remove based on current state, mark them with value `2`
2. **Remove phase:** Change all marked cells to `0`

This ensures all neighbor counts are computed based on the same grid state.

**Teaching Point:** In simulation problems, when the new state depends on the current state of multiple cells, you often need to separate the "calculate new state" phase from the "apply new state" phase. This is a fundamental pattern in cellular automaton simulations.

## AP CSA Learning Objectives

This solution addresses the following official AP Computer Science A Learning Objectives (2025):

- **LO 1.3.C:** Develop code for arithmetic expressions (array index arithmetic, counting).
- **LO 2.3.A:** Develop code to represent branching logical processes (`if` statements with compound conditions).
- **LO 2.7.B:** Develop code to represent iterative processes using `while` loops (outer simulation loop).
- **LO 2.8.A:** Develop code to represent iterative processes using `for` loops (grid traversal).
- **LO 4.1.A:** Develop code to represent collections of related primitive data using 2D array objects (`int[][]`).
- **LO 4.2.A:** Develop code to traverse 2D array objects using nested iteration statements.
- **LO 4.9.A:** Develop code used to traverse the elements of an `ArrayList` (input traversal).

## Teaching Notes

- **2D Array Padding:** The technique of adding a border of zeros around a 2D array is a common optimization that eliminates boundary checks. This is worth highlighting as a practical design pattern.

- **Cellular Automaton Simulation:** This problem introduces students to cellular automaton concepts where each cell's next state depends on its neighbors. Conway's Game of Life is a famous example of this pattern.

- **Two-Phase Update Pattern:** The mark-then-remove approach is essential for many grid-based simulations. Students should understand *why* this is necessary, not just memorize the pattern.

- **Debugging with Print Statements:** During development, printing the grid state after each iteration can help visualize the simulation and catch errors. This is a valuable debugging technique for spatial problems.

- **Neighbor Counting:** The `numberNeighbors` method demonstrates clean arithmetic without complex conditionals. The sum of 8 adjacent cells is straightforward when boundaries are pre-handled with padding.

## Algorithm Complexity

- **Part 1:** $O(n^2)$ where $n$ is the grid width—single pass through all cells with constant-time neighbor counting.
- **Part 2:** $O(k \times n^2)$ where $k$ is the number of removal iterations and $n$ is the grid width. In the worst case, $k$ could be $O(n)$ if rolls are removed one layer at a time from the outside in, giving $O(n^3)$ total complexity.

## Mathematical Representation

The neighbor count for a cell at position $(r, c)$ in a grid $W$ is:

$$N(r,c) = \sum_{i=-1}^{1} \sum_{j=-1}^{1} W[r+i][c+j] - W[r][c]$$

However, since we only count neighbors (excluding the cell itself), the implementation sums the 8 surrounding cells explicitly.

---

**Summary:** Day 4 demonstrates 2D array manipulation, cellular automaton simulation, and the importance of proper state update sequencing. The debugging process highlights common pitfalls in grid-based simulations and reinforces the value of carefully reading problem requirements.

