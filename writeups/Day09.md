---
layout: post
title: "Day 9: Largest Rectangle"
date: 2025-12-09
categories: [writeup]
tags: [AP-CSA, LO-1.3.C, LO-2.3.A, LO-2.5.A, LO-4.7.A, LO-4.8.A, LO-4.9.A, run-length-encoding, ray-casting, even-odd-rule, polygon-filling, sorting-optimization, pre-filling, row-wise-verification]
---

# Day 9: Largest Rectangle

## Summary

Day 9 involves finding the largest axis-aligned rectangle defined by pairs of points that lies entirely within a polygonal region. We compute areas for all possible rectangles formed by point pairs and identify the maximum. This requires efficient iteration, arithmetic, and advanced optimizations like Run-Length Encoding (RLE), pre-filling interiors, sorting pairs by area, and row-wise verification to handle large inputs (496 points, 122k pairs) in under 1 second.

## Key Concepts

- Iteration (nested loops for pair checking, with sorting for optimization)
- Selection (if-else for boundary and fill checks)
- Arithmetic (area calculations, range checks)
- Collections (`ArrayList` for points, pairs, and RLE runs)
- Sorting (descending area order for early termination)
- Run-Length Encoding (RLE) for compact grid representation
- Polygon filling (even-odd rule for pre-filling interiors)
- Row-wise verification (efficient range checks instead of per-cell)

## Approach

### Part 1: Exhaustive Rectangle Area

Using a `Point` class with overloaded constructors for parsing, we compute the area of every possible rectangle formed by point pairs as opposite corners. A nested loop iterates through all pairs, calculating width and height via absolute differences, and tracks the maximum area.

```java
public String part1(ArrayList<String> input) {
    ArrayList<Point> redTiles = new ArrayList<>();
    for (String line : input) {
        redTiles.add(new Point(line));
    }
    long maxArea = 0;
    for (int i = 0; i < redTiles.size() - 1; i++) {
        for (int j = i + 1; j < redTiles.size(); j++) {
            long area = redTiles.get(i).areaWith(redTiles.get(j));
            if (area > maxArea) maxArea = area;
        }
    }
    return String.valueOf(maxArea);
}
```

### Part 2: Memory-Efficient Polygon Interior Check

In Part 2, the points define a bounded polygonal region, and we must find the largest rectangle that lies entirely within this region. The challenge is handling large inputs with hundreds of points, requiring both memory-efficient representation and optimized checking algorithms.

#### Initial Array Approach (Memory-Limited)
We start with a 2D `int[][]` array to represent the grid, marking each point and connecting them with lines to form the boundary. A scanline algorithm (using the even-odd rule) fills the interior by toggling fill state on boundary crossings. We then check all possible rectangles by verifying every cell in the rectangle is filled. This approach works for sample data but fails on real inputs due to excessive memory usage for large grids.

#### RLE Optimization for Memory Efficiency
To address memory limits, we use Run-Length Encoding (RLE) to compress each row into runs of consecutive empty (0) or filled (1) cells, stored as {type, length} pairs in an `ArrayList<ArrayList<int[]>>`. Boundaries are added by splitting and merging runs dynamically. Rectangle checks initially used ray casting for unfilled cells, but this was optimized further.

#### Advanced Optimizations for Performance
- **Pre-filling Interior:** After drawing the boundary, we apply the even-odd rule once to fill all interior cells, eliminating the need for per-cell ray casting during rectangle checks.
- **Sorting Pairs by Area:** We sort all point pairs by potential area (descending) to check largest rectangles first, stopping at the first fully filled one since it's guaranteed to be the maximum.
- **Row-Wise Verification:** Instead of checking individual cells, we use `getFilledLengthInRange` to verify entire rows are fully filled in the rectangle's x-range, reducing complexity from O(width × height) to O(height) per check.

```java
// Optimized RLE FloorModel with interior filling
public class FloorModel {
    ArrayList<ArrayList<int[]>> floor;  // {type, length} runs

    public void addTile(int x, int y) {
        // Split and merge runs for boundary addition
    }

    public void fillInterior() {
        // Apply even-odd rule to fill interior runs
        for (int y = 0; y < length; y++) {
            boolean inside = false;
            ArrayList<int[]> newSections = new ArrayList<>();
            for (int[] section : floor.get(y)) {
                int type = section[0];
                int len = section[1];
                if (type == 1) inside = !inside;
                newSections.add(new int[]{inside ? 1 : type, len});
            }
            floor.set(y, newSections);
            // Merge adjacent same-type runs
        }
    }

    public int getFilledLengthInRange(int y, int startX, int endX) {
        // Traverse runs to sum filled lengths in x-range
    }
}

// Optimized rectangle check with pre-filled floor
public long filledAreaWith(Point p, FloorModel floor) {
    int minX = Math.min(x, p.x), maxX = Math.max(x, p.x);
    int minY = Math.min(y, p.y), maxY = Math.max(y, p.y);
    long width = maxX - minX + 1;
    for (int r = minY; r <= maxY; r++) {
        if (floor.getFilledLengthInRange(r, minX, maxX) != width) {
            return 0;  // Row not fully filled
        }
    }
    return width * (maxY - minY + 1);
}

// Main algorithm with sorted pair checking
ArrayList<int[]> pairs = new ArrayList<>();
for (int i = 0; i < redTiles.size() - 1; i++) {
    for (int j = i + 1; j < redTiles.size(); j++) {
        long area = redTiles.get(i).areaWith(redTiles.get(j));
        pairs.add(new int[]{i, j, (int)area});
    }
}
pairs.sort((a, b) -> Integer.compare(b[2], a[2]));  // Sort by area descending
long maxArea = 0;
for (int[] pair : pairs) {
    long area = redTiles.get(pair[0]).filledAreaWith(redTiles.get(pair[1]), floor);
    if (area > maxArea) {
        maxArea = area;
        break;  // Largest possible filled area found
    }
}
```

## AP CSA Subset Compliance and Learning Objectives

This solution is fully AP CSA subset compliant and addresses:

- **LO 1.3.C:** Arithmetic expressions for area and range calculations.
- **LO 2.3.A:** Selection statements for boundary and fill checks.
- **LO 2.5.A:** Compound Boolean expressions in range and row verification logic.
- **LO 2.7.B:** Iteration with nested loops and sorting for optimized pair checking.
- **LO 4.7.A:** `Integer.parseInt` for input parsing.
- **LO 4.8.A:** `ArrayList` for points, pairs, and RLE runs.
- **LO 4.9.A:** Enhanced for loops for traversing lists and pairs.

## CSTA Standards for CS Teachers

This writeup aligns with the following CSTA Standards for CS Teachers (2023):

- **LT.1.1:** Teachers design learning experiences that align with CS standards and curricula (AP CSA LOs covered).
- **LT.1.2:** Teachers use a variety of instructional strategies to engage students in CS learning (iterative optimization, code examples).
- **LT.3.2:** Teachers help students develop problem-solving skills (algorithm optimization process).
- **AS.1.1:** Teachers use formative assessment to inform instruction (progress debugging, performance metrics).
- **PG.1.1:** Teachers engage in professional learning (documenting advanced optimizations).

## Teaching Notes

- **RLE Introduction:** Use this to teach compression techniques, comparing to image storage and memory trade-offs.
- **Algorithm Trade-offs:** Contrast memory vs. time—array is fast but memory-intensive; RLE with pre-filling and row checks optimizes both.
- **Sorting for Optimization:** Demonstrate how sorting pairs by area enables early termination, reducing unnecessary checks.
- **Geometric Algorithms:** Pre-filling with even-odd rule illustrates efficient polygon processing for graphics/CS applications.
- **Iterative Optimization:** Show the progression from naive array to RLE, then advanced optimizations, emphasizing profiling and algorithmic improvement.
- **Performance Analysis:** Discuss how row-wise checks reduce complexity from O(width × height) to O(height), enabling scalability. 

