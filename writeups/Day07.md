---
layout: post
title: "Day 7: Tachyon Beam Splitters"
date: 2025-12-07
categories: [writeup]
tags: [AP-CSA, CSTA, beyond-subset, recursion, memoization, dynamic-programming, performance-optimization, CON-2.B, VAR-2.E, MOD-3.B, HashSet, HashMap, call-stack]
---

# Day 7: Recursion and Memoization - Beyond AP CSA 2025

> **Note:** This writeup was generated with assistance from GitHub Copilot (Claude Sonnet 4.5) to document the solution and pedagogical approach.

## The 2025 AP CSA Curriculum Change

In summer 2025, the College Board revised AP Computer Science A, removing Unit 9 (Recursion and Inheritance) from the curriculum. Starting with the May 2026 exam, **recursion is no longer part of AP CSA**. The College Board stated this change would "more closely align the course with introductory college courses and allow teachers to cover other topics in more detail."

**However**, research on college-level CS1 curricula shows recursion remains a core topic. A 2025 replication study surveying 178 CS1 educators (university and secondary school) found that college-level educators continue teaching recursion as they have for 20 years, though it remains challenging to teach. Notably, the study found "most secondary school educators perceiving it does not belong in CS1," suggesting alignment between the AP CSA curriculum change and high school teaching practices, while college CS1 courses continue to include recursion (Garcia & Craig, 2025).

Recursion is essential for understanding:
- Tree and graph algorithms
- Divide-and-conquer strategies
- Dynamic programming
- Functional programming
- Recursive data structures

This writeup demonstrates why recursion remains valuable for students planning to study computer science in college, even though it's no longer on the AP exam.

---

## Problem Summary

Day 7 presents a physics simulation: tachyon beams travel through a field of beam splitters. The beams:
- Start at position 'S' in row 0
- Move downward one row at a time
- Continue straight through empty spaces ('.')
- Split into two beams (left and right) when hitting a splitter ('^')

**Part 1**: Count how many splitters the beam(s) encounter from start to finish.

**Key Insight**: Multiple beams can hit the same splitter, but we only count each splitter once.

---

## Mathematical Foundations: Sequences vs. Functions

### Iteration: The Sequence Perspective

In mathematics, a **sequence** is defined iteratively:
$$a_n = a_{n-1} + d \quad \text{(arithmetic sequence)}$$
$$a_n = r \cdot a_{n-1} \quad \text{(geometric sequence)}$$

You start with an initial value and repeatedly apply a rule to generate the next term. This is how loops work in programming:

```java
// Iterative factorial - sequence perspective
int factorial(int n) {
    int result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;  // Each step depends on previous result
    }
    return result;
}
```

### Recursion: The Function Perspective

In mathematics, many concepts are defined **recursively** - in terms of themselves:

**Factorial**:
$$n! = \begin{cases} 1 & \text{if } n = 0 \\ n \cdot (n-1)! & \text{if } n > 0 \end{cases}$$

**Derivatives** (from calculus):
$$\frac{d}{dx}[f(x) \cdot g(x)] = f(x) \cdot g'(x) + f'(x) \cdot g(x)$$

The derivative of a product is defined in terms of the derivatives of its parts - recursive thinking!

**Fibonacci sequence**:
$$F(n) = F(n-1) + F(n-2), \quad F(0) = 0, F(1) = 1$$

This is the **functional perspective**: define a solution in terms of simpler versions of the same problem.

```java
// Recursive factorial - functional perspective
int factorial(int n) {
    if (n == 0) return 1;           // Base case
    return n * factorial(n - 1);     // Recursive case
}
```

### The Connection to Calculus

Calculus is fundamentally recursive:

**Integration** is defined recursively:
$$\int u \, dv = uv - \int v \, du \quad \text{(integration by parts)}$$

The solution to one integral depends on solving another integral!

**Power series** expand functions recursively:
$$e^x = 1 + x + \frac{x^2}{2!} + \frac{x^3}{3!} + \cdots = \sum_{n=0}^{\infty} \frac{x^n}{n!}$$

Each term is defined in terms of the previous term, building toward the complete function.

**Key Teaching Point**: Recursion isn't just a programming trick - it's a fundamental mathematical concept. When you see "$f(n)$ defined in terms of $f(n-1)$", you're seeing recursive thinking.

---

## Recursion Fundamentals

Before diving into recursive solutions, let's establish the essential components every recursive function needs:

### The Three Essential Components

**1. Base Case (Stopping Condition)**
- The simplest case that can be solved directly without recursion
- Prevents infinite recursion by providing an "exit condition"
- Example: In factorial, $0! = 1$ is the base case

**2. Recursive Case**
- Breaks the problem into smaller subproblems of the same type
- Must make progress toward the base case
- Example: In factorial, $n! = n \times (n-1)!$ breaks the problem down

**3. Return and Combine**
- Each recursive call returns a result
- Results are combined to solve the original problem
- Example: Multiply $n$ by the result of $(n-1)!$

### A Simple Example: Factorial

```java
public static int factorial(int n) {
    // Base case: simplest problem we can solve directly
    if (n == 0 || n == 1) {
        return 1;
    }
    
    // Recursive case: break problem into smaller subproblem
    // Progress toward base case: n becomes n-1
    return n * factorial(n - 1);
}
```

**Call Stack Visualization** for `factorial(4)`:
```
factorial(4)
  → 4 * factorial(3)
         → 3 * factorial(2)
                → 2 * factorial(1)
                       → 1 (base case!)
                   2 * 1 = 2
            3 * 2 = 6
     4 * 6 = 24
```

### Common Pitfalls

**Missing Base Case** leads to infinite recursion:
```java
// WRONG: No stopping condition!
public static int badFactorial(int n) {
    return n * badFactorial(n - 1);  // Stack overflow!
}
```

**Base Case Never Reached** due to incorrect progress:
```java
// WRONG: n never reaches 0!
public static int badFactorial(int n) {
    if (n == 0) return 1;
    return n * badFactorial(n + 1);  // Getting larger, not smaller!
}
```

**Not Making Progress** toward base case:
```java
// WRONG: n - 0 doesn't change!
public static int badFactorial(int n) {
    if (n == 0) return 1;
    return n * badFactorial(n - 0);  // Infinite recursion!
}
```

### Recursion vs. Iteration: When to Use Each

**Use Recursion When**:
- Problem naturally divides into similar subproblems (tree traversal, divide-and-conquer)
- Solution structure mirrors the problem structure (directory tree, nested data)
- Backtracking is needed (maze solving, game playing)

**Use Iteration When**:
- Simple sequential processing (summing array elements)
- Memory efficiency is critical (recursion uses call stack)
- Tail recursion isn't optimized (Java doesn't optimize tail calls)

### Key Insight for This Problem

In our tachyon beam problem:
- **Base case**: Reaching the bottom row (no more rows to process)
- **Recursive case**: Process current row, recurse to next row
- **Progress**: Row number increases until we reach the bottom
- **Return**: Count of splitters encountered in all remaining rows

---

## Part 1: Counting Splitters - Iterative vs Recursive

### Problem Statement

Given a field of tachyon beam splitters, count how many unique splitters are encountered by beam(s) traveling from start 'S' to the bottom. Beams move downward, continuing straight through empty spaces ('.') and splitting left/right at splitters ('^').

**Sample Input**:
```
.S.
...
.^.
^.^
...
```

**Sample Answer**: 21 unique splitters encountered

### The Iterative Solution (AP CSA Compliant)

The iterative approach simulates beam movement row-by-row, tracking which columns contain beams:

```java
private String part1Iterative(ArrayList<String> input) {
    int[][] tachyonField = parseInput(input);
    int start = input.get(0).indexOf("S");
    
    int row = 1;
    int splitCount = 0;
    ArrayList<Integer> beamColumns = new ArrayList<>();
    ArrayList<Integer> nextBeamColumns = new ArrayList<>();
    nextBeamColumns.add(start);
    
    while (row < tachyonField.length - 1) {
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

private boolean addIfUnique(ArrayList<Integer> list, Integer column) {
    if (list.contains(column)) return false;
    list.add(column);
    return true;
}
```

**Key Characteristics**:
- State tracked explicitly in variables (`beamColumns`, `row`)
- Loop-based progression through the field
- Manual bookkeeping with ArrayLists
- Sequential processing: row 1, then row 2, then row 3...

### The Recursive Solution (Beyond AP CSA)

The recursive approach thinks functionally: "splits from this position = splits here + splits from next positions":

```java
private String part1Recursive(ArrayList<String> input) {
    int[][] tachyonField = parseInput(input);
    int start = input.get(0).indexOf("S");
    
    HashSet<String> visitedSplitters = new HashSet<>();
    HashSet<String> visitedPositions = new HashSet<>();
    countSplitsRecursive(tachyonField, 1, start, visitedSplitters, visitedPositions);
    return String.valueOf(visitedSplitters.size());
}

private void countSplitsRecursive(int[][] field, int row, int col,
                                 HashSet<String> visitedSplitters,
                                 HashSet<String> visitedPositions) {
    // Base case: reached bottom or out of bounds
    if (row >= field.length || col < 0 || col >= field[0].length) {
        return;
    }
    
    // Avoid infinite loops from converging paths
    String posKey = row + "," + col;
    if (visitedPositions.contains(posKey)) {
        return;
    }
    visitedPositions.add(posKey);
    
    // Process current position
    if (field[row][col] == EMPTY) {
        countSplitsRecursive(field, row + 1, col, visitedSplitters, visitedPositions);
    } else if (field[row][col] == SPLITTER) {
        visitedSplitters.add(posKey);
        // Recurse on both branches
        countSplitsRecursive(field, row + 1, col - 1, visitedSplitters, visitedPositions);
        countSplitsRecursive(field, row + 1, col + 1, visitedSplitters, visitedPositions);
    }
}
```

**Key Characteristics**:
- State tracked on the **call stack** (implicit)
- Function calls itself with different positions
- Base cases define when to stop
- Natural representation of tree structure (beam splits create tree branches)

### When to Use Each Approach

**Use Iteration When:**
- Simple sequential processing (reading through an array)
- State is simple (a counter, an index)
- Thinking in terms of "do this N times"
- Example: Factorial, Fibonacci, summing array elements

**Use Recursion When:**
- Problem has a natural tree/graph structure
- Problem can be broken into smaller versions of itself
- "Solution for N depends on solution for N-1" thinking
- Example: Tree traversal, graph search, divide-and-conquer

**Part 1 Lesson**: The beam splitting creates a tree structure, making recursion a natural fit. However, the iterative solution is also clear and arguably easier to debug for beginners.

### The Critical Pitfall: Tracking Visited Positions

**Initial Bug**: The first recursive version only tracked visited *splitters*, not all visited *positions*. This caused infinite loops:

```
    S
    .
   .^.
  ^.^.^
```

Without tracking all visited positions, beams from the left branch and right branch could both visit the same empty cell, each recursing into the same subtree infinitely.

**The Fix**: Track ALL visited positions, not just splitters. This prevents re-visiting positions where paths converge.

**Teaching Moment**: This is a classic recursion pitfall - failing to prevent infinite loops when different recursive branches can reach the same state. Graph algorithms (DFS, BFS) always need visited tracking for this reason.

---

## Part 2: Counting Paths - The Power of Memoization

### Problem Statement

Count the total number of distinct paths from the start position to *any* exit at the bottom of the field. Each split creates two independent paths that must both be counted.

**Sample Answer**: 40 distinct paths

### Why This Is Hard: Exponential Explosion

Without optimization, this problem has **exponential time complexity**:
- Each splitter doubles the number of paths
- 10 splitters in a chain → $2^{10} = 1,024$ paths
- 20 splitters → $2^{20} = 1,048,576$ paths
- 30 splitters → $2^{30} = 1,073,741,824$ paths (over 1 billion!)

The real puzzle input has a deep tree structure. A naive recursive solution would timeout trying to count all paths.

### Memoization: The Key Insight

**Memoization** is the technique of caching function results to avoid redundant computation. It's named after the Latin "memorandum" (to be remembered).

**The Key Observation**: Multiple paths can reach the same position, but once we know "paths from position P to the bottom", we never need to recalculate it!

```
    S
   / \
  A   B
   \ / \
    C   D
```

If paths from A and B both reach C, we calculate "paths below C" only once and reuse the result.

### The Memoized Recursive Solution

```java
private String part2Recursive(ArrayList<String> input) {
    int[][] tachyonField = parseInput(input);
    int start = input.get(0).indexOf("S");
    
    HashMap<String, Long> cache = new HashMap<>();
    long result = countPathsRecursive(tachyonField, 0, start, cache);
    return String.valueOf(result);
}

private long countPathsRecursive(int[][] field, int row, int col, 
                                HashMap<String, Long> cache) {
    // Base case: out of bounds
    if (row >= field.length || col < 0 || col >= field[0].length) {
        return 0;
    }
    
    // Base case: reached bottom row
    if (row == field.length - 1) {
        return 1;
    }
    
    // Check cache
    String key = row + "," + col;
    if (cache.containsKey(key)) {
        return cache.get(key);
    }
    
    // Calculate paths
    long paths = 0;
    if (field[row + 1][col] == EMPTY) {
        paths = countPathsRecursive(field, row + 1, col, cache);
    } else if (field[row + 1][col] == SPLITTER) {
        paths = countPathsRecursive(field, row + 1, col - 1, cache) +
                countPathsRecursive(field, row + 1, col + 1, cache);
    }
    
    cache.put(key, paths);
    return paths;
}
```

**How Memoization Works**:
1. Before computing paths from position (row, col), check if we've already computed it
2. If yes, return the cached result immediately
3. If no, compute the result recursively
4. Store the result in the cache before returning
5. Future calls with the same position use the cached value

**Time Complexity**: 
- Without memoization: $O(2^n)$ where n is the number of splitters (exponential)
- With memoization: $O(rows \times columns)$ - each position computed at most once (polynomial)

This transforms an impossible problem into a solvable one!

### The Iterative Solution (Bottom-Up Dynamic Programming)

The iterative solution processes the field bottom-up, building path counts from the bottom to the top:

```java
private String part2Iterative(ArrayList<String> input) {
    int[][] tachyonField = parseInput(input);
    int start = input.get(0).indexOf("S");
    
    ArrayList<Point> particleLocations = new ArrayList<>();
    HashMap<Point, Long> pathsBelow = new HashMap<>();
    particleLocations.add(new Point(0, start));
    
    while (particleLocations.size() > 0) {
        int lastIndex = particleLocations.size() - 1;
        Point current = particleLocations.get(lastIndex);
        
        int nextRow = current.row + 1;
        if (nextRow >= tachyonField.length) {
            pathsBelow.put(current, 1L);
            particleLocations.remove(lastIndex);
            continue;
        }
        
        Point next = new Point(nextRow, current.column);
        Point right = new Point(nextRow, current.column + 1);
        Point left = new Point(nextRow, current.column - 1);
        
        if (tachyonField[nextRow][current.column] == EMPTY) {
            if (pathsBelow.containsKey(next)) {
                pathsBelow.put(current, pathsBelow.get(next));
                particleLocations.remove(lastIndex);
            } else {
                particleLocations.add(next);
            }
        } else if (tachyonField[nextRow][current.column] == SPLITTER) {
            boolean descended = false;
            long lowerPaths = 0;
            
            if (pathsBelow.containsKey(right)) {
                lowerPaths = pathsBelow.get(right);
            } else {
                particleLocations.add(right);
                descended = true;
            }
            
            if (pathsBelow.containsKey(left)) {
                lowerPaths += pathsBelow.get(left);
            } else {
                particleLocations.add(left);
                descended = true;
            }
            
            if (!descended) {
                pathsBelow.put(current, lowerPaths);
                particleLocations.remove(lastIndex);
            }
        }
    }
    
    return String.valueOf(pathsBelow.get(new Point(0, start)));
}
```

**Key Difference**: The iterative solution uses an explicit stack (ArrayList) to track positions to process, while the recursive solution uses the call stack implicitly.

### The True Bottom-Up Solution (Table-Based Dynamic Programming)

There's a third approach that's even more direct - a classic DP table that fills from bottom to top:

```java
private String part2BottomUp(ArrayList<String> input) {
    int[][] tachyonField = parseInput(input);
    int start = input.get(0).indexOf("S");
    
    int rows = tachyonField.length;
    int cols = tachyonField[0].length;
    
    // Create DP table: dp[row][col] = number of paths from (row,col) to bottom
    long[][] dp = new long[rows][cols];
    
    // Base case: bottom row - all positions have 1 path
    for (int col = 0; col < cols; col++) {
        dp[rows - 1][col] = 1;
    }
    
    // Fill table bottom-up (from second-to-last row to top)
    for (int row = rows - 2; row >= 0; row--) {
        for (int col = 0; col < cols; col++) {
            int nextRow = row + 1;
            
            if (tachyonField[nextRow][col] == EMPTY) {
                // Continue straight
                dp[row][col] = dp[nextRow][col];
            } else if (tachyonField[nextRow][col] == SPLITTER) {
                // Split left and right
                long leftPaths = (col > 0) ? dp[nextRow][col - 1] : 0;
                long rightPaths = (col < cols - 1) ? dp[nextRow][col + 1] : 0;
                dp[row][col] = leftPaths + rightPaths;
            }
        }
    }
    
    return String.valueOf(dp[0][start]);
}
```

**Key Characteristics**:
- **No recursion**: Pure iterative approach
- **No stack management**: Direct table access
- **Cache-friendly**: Sequential memory access pattern
- **Predictable**: No hash map lookups, just array indexing
- **Space explicit**: $O(rows \times columns)$ memory clearly allocated

### Top-Down vs Bottom-Up: Two Sides of Dynamic Programming

**Top-Down (Recursive + Memoization)**:
- Start with the problem you want to solve
- Break into subproblems naturally
- Cache results as you go
- Code matches problem structure directly
- Only computes what's needed (lazy evaluation)

**Bottom-Up (Iterative DP Table)**:
- Start with base cases (bottom of field)
- Build up to final answer systematically
- No recursion overhead
- All subproblems computed (even if not needed)
- More predictable memory access patterns

**Stack-Based Iterative** (the original `part2Iterative`):
- Hybrid approach: iterative but mimics recursion
- Explicit stack management
- More complex state tracking
- Flexibility of iterative with structure of recursive

All three approaches achieve the same time complexity: $O(rows \times columns)$.

---

## Performance Analysis: The Surprising Results

### Setup: Fair Comparison

To compare implementations fairly:
- Warmup run for each solution (JVM optimization)
- 1000 iterations of each solution
- Measure average execution time
- Test on real puzzle input (large dataset)

### Results: Part 1

```
Part 1 Timing (1000 iterations):
  Iterative: 261.611 µs average
  Recursive: 655.147 µs average
  Speedup: 2.50x (iterative is faster)
```

**Why is iterative faster for Part 1?**
- Simple sequential processing
- No function call overhead
- ArrayList operations optimized for sequential access
- HashSet operations (visited tracking) are the bottleneck in recursive version

### Results: Part 2 - Three Approaches Compared

```
Part 2 Timing (1000 iterations):
  Iterative (Stack): 881.295 µs average
  Recursive (Memo):  525.217 µs average
  Bottom-Up DP:       92.139 µs average
  
  Speedup (Stack vs Memo):  1.68x (memoization wins!)
  Speedup (Stack vs DP):    9.56x (DP destroys stack-based!)
  Speedup (Memo vs DP):     5.70x (DP beats memoization!)
  
  All methods produce identical results: ✓ Verified
```

**Bottom-Up DP is the clear winner** - nearly **10x faster** than the stack-based approach and **6x faster** than recursive memoization!

**Why is recursive faster for Part 2?**
- Less object creation overhead
- Call stack operations are highly optimized
- Simpler cache key (String) vs complex Point objects
- No manual stack management

**This surprised me!** The conventional wisdom is "recursion is slower due to function call overhead." But in practice, when the iterative solution requires complex data structure management, recursion can actually be faster.

### Performance Optimizations: Making Iterative Competitive

Starting from the original iterative solution (1257 µs), I applied three optimizations:

**Optimization #1: Primitive `int` Instead of `Integer` in Point**
```java
// Before:
class Point {
    Integer row, column;
    public boolean equals(Object o) {
        return Objects.equals(row, point.row) && Objects.equals(column, point.column);
    }
}

// After:
class Point {
    int row, column;
    public boolean equals(Object o) {
        return row == point.row && column == point.column;
    }
}
```

**Result**: 1257 µs → 1134 µs (9.8% faster)

**Why?**: Eliminated boxing/unboxing overhead. Integer objects require heap allocation; primitives are stored directly.

**Optimization #2: `containsKey()` Instead of `.keySet().contains()`**
```java
// Before:
if (pathsBelow.keySet().contains(next)) { ... }

// After:
if (pathsBelow.containsKey(next)) { ... }
```

**Result**: 1134 µs → 1115 µs (1.7% faster)

**Why?**: `.keySet()` creates a Set view object on every call. `containsKey()` is a direct HashMap operation.

**Optimization #3: ArrayList Operations from Back Instead of Front**
```java
// Before (O(n) operations):
Point current = particleLocations.get(0);
particleLocations.remove(0);      // Shifts all elements!
particleLocations.add(0, next);    // Shifts all elements!

// After (O(1) operations):
int lastIndex = particleLocations.size() - 1;
Point current = particleLocations.get(lastIndex);
particleLocations.remove(lastIndex);  // No shifting!
particleLocations.add(next);           // No shifting!
```

**Result**: 1115 µs → 875 µs (21.5% faster)

**Why?**: Removing from the front of ArrayList requires shifting all elements (O(n)). Removing from the back is O(1).

### Why Is Bottom-Up DP So Much Faster?

**1. No Hash Function Overhead**
- Stack-based: HashMap with Point objects (hash computation + equality checks)
- Memoization: HashMap with String keys (hash computation on "row,col")
- Bottom-up DP: Direct 2D array access `dp[row][col]` (simple arithmetic)

**2. Cache-Friendly Memory Access**
- Stack-based: Scattered HashMap accesses (cache misses)
- Memoization: Scattered HashMap accesses (cache misses)
- Bottom-up DP: Sequential array access, row by row (cache hits!)

**3. No Object Allocation**
- Stack-based: Creates Point objects, ArrayList growth, HashMap entries
- Memoization: Creates String keys, HashMap entries, recursive stack frames
- Bottom-up DP: Single `long[][]` array allocated once

**4. Predictable for JVM**
- Stack-based: Complex branching logic, unpredictable memory access
- Memoization: Function calls, cache checks, stack frame overhead
- Bottom-up DP: Simple nested loops, JIT compiler can optimize aggressively

### The Surprising Truth About Dynamic Programming

This problem beautifully demonstrates a counter-intuitive lesson:

**"More code" doesn't mean "slower code"**

The stack-based iterative solution seemed clever - it avoids recursion! But it's actually the **slowest** approach because:
1. It mimics recursion's control flow without JVM optimizations
2. It uses HashMaps when simple arrays would suffice
3. It manages an explicit stack poorly (ArrayList operations)

**"Elegant recursion" isn't always optimal**

The recursive memoization is clean and intuitive, but HashMap overhead adds up. When you call a function thousands of times, even small costs matter.

**"Classic DP table" wins for a reason**

The textbook bottom-up DP approach exists for a reason - it's optimized for how computers actually work:
- Sequential memory access (cache-friendly)
- Simple array indexing (no hashing)
- Predictable loops (JIT optimization)
- No allocation overhead (one array, done)

### Performance Summary Table

| Approach | Avg Time | vs Fastest | Key Bottleneck |
|----------|----------|------------|----------------|
| Bottom-Up DP | **92.1 µs** | **1.00x** | Array allocation |
| Recursive Memo | 525.2 µs | 5.70x | HashMap lookups |
| Stack-Based | 881.3 µs | 9.56x | HashMap + ArrayList ops |

**Key Insight**: For DP problems with a clear grid/table structure, **use a table**. The classic approach is classic for a reason!

The three optimizations demonstrate a crucial programming principle: **Algorithm complexity is important, but constant factors matter in practice.**

**Understanding ArrayList performance**:
- `add(item)` at end: O(1) amortized
- `add(0, item)` at front: O(n) - must shift all elements right
- `remove(size-1)` from end: O(1)
- `remove(0)` from front: O(n) - must shift all elements left
- `get(i)`: O(1) - random access array

**Key Teaching Moment**: Even with the same big-O complexity, implementation details can cause 30%+ performance differences. The "front vs back" optimization alone gave a 21.5% speedup!

---

## Beyond AP CSA: What Students Need to Know

### Concepts Not in AP CSA (But Essential for College CS)

1. **Recursion** (removed in 2025)
   - Base cases and recursive cases
   - Call stack mechanics
   - Recursive tree structures

2. **Memoization / Dynamic Programming**
   - Top-down (recursive + cache)
   - Bottom-up (iterative table-filling)
   - Time/space tradeoffs

3. **HashSet and HashMap** (in Java but not deeply covered)
   - O(1) average-case lookup
   - Hash functions and collision handling
   - When to use vs ArrayList

4. **Performance Analysis**
   - Big-O complexity
   - Constant factors in practice
   - Profiling and optimization

5. **Call Stack vs Heap**
   - Where local variables live
   - Function call overhead
   - Stack overflow vs heap allocation

### The Recursion "Click" Moment

Many students struggle with recursion initially, then suddenly it "clicks." The key insights:

1. **Trust the recursion**: Don't try to trace every call mentally. Trust that `factorial(n-1)` works, focus on using its result correctly.

2. **Base cases are crucial**: Every recursion needs a stopping point. Ask "what's the simplest input?"

3. **Make progress toward base case**: Each recursive call must get "smaller" in some meaningful way.

4. **State management**: Decide what to pass as parameters vs what to track globally.

For Day 7 Part 1, the "progress" is moving down rows (toward the bottom). The base case is "out of bounds."

---

## AP CSA Subset Compliance

### What's AP CSA Compliant

**Part 1 (Iterative Solution)**:
- ✅ ArrayList for dynamic collections
- ✅ Enhanced for loops
- ✅ 2D arrays
- ✅ Helper methods
- ✅ Boolean logic and conditionals

**Data Type Exception**:
- ❌ `long` used instead of `int` (paths exceed 2 billion)
- **Justification**: Problem requires it, similar to Day 3

### What's Beyond AP CSA

**Part 1 (Recursive Solution)**:
- ❌ Recursion (removed from 2025 curriculum)
- ❌ HashSet (Java standard library but not emphasized in AP CSA)
- ❌ Multiple simultaneous data structures (two HashSets)

**Part 2 (Both Solutions)**:
- ❌ Memoization / Dynamic programming concepts
- ❌ HashMap with custom objects as keys
- ❌ Advanced performance optimization techniques

### Why Include Beyond-Subset Content?

1. **College Preparation**: These concepts appear in CS1 courses
2. **Real-World Relevance**: Professional code uses recursion extensively
3. **Problem-Solving Power**: Some problems are much clearer with recursion
4. **Historical Context**: Students should know what was removed and why it matters

---

## Class Structure and Design

```java
public class Day07 extends Day {
    private static final int EMPTY = '.';
    private static final int SPLITTER = '^';
    
    // Part 1: Switch between implementations
    public String part1(ArrayList<String> input) {
        boolean useRecursive = false;  // Toggle to compare
        return useRecursive ? part1Recursive(input) : part1Iterative(input);
    }
    
    // Part 2: Switch between implementations
    public String part2(ArrayList<String> input) {
        boolean useRecursive = false;  // Toggle to compare
        return useRecursive ? part2Recursive(input) : part2Iterative(input);
    }
    
    // Inner class for position tracking
    public class Point {
        int row, column;  // Primitive ints for performance
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return row == point.row && column == point.column;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
```

**Design Decisions**:
1. **Dual implementations**: Students can compare recursive vs iterative directly
2. **Toggle flags**: Easy to switch and benchmark
3. **Inner Point class**: Encapsulates position with proper equals/hashCode
   - *Note*: Inner classes are **not in the AP CSA subset**. In production code, `Point` would typically be a separate top-level class. However, keeping it as an inner class here serves two pedagogical purposes: (1) it keeps all related code in one file for easier demonstration, and (2) it introduces students to inner classes as a useful organizational technique they'll encounter in college CS courses and professional development.
4. **Constants for characters**: Makes code self-documenting

---

## AP CSA Learning Objectives

Although recursion is no longer in AP CSA, the iterative solutions still address official learning objectives:

- **CON-2.B:** Represent iterative processes using iteration statements
  - Both solutions use nested loops for field processing
  - Part 2 uses while loop with manual stack management

- **VAR-2.E:** Represent collections of related primitive data using 2D array objects
  - Field represented as `int[][]` for efficient character storage
  - Nested array access for position lookup

- **MOD-3.B:** Call methods of class object
  - ArrayList methods: `add()`, `remove()`, `get()`, `size()`, `contains()`
  - HashMap methods: `put()`, `get()`, `containsKey()`
  - HashSet methods: `add()`, `contains()`

**Note**: The recursive solutions demonstrate learning objectives that *were* in AP CSA through 2024 but are no longer tested.

---

## CSTA Standards for CS Teachers

This writeup aligns with the **CSTA Standards for CS Teachers (2020)**:

**Standard 2: Content Knowledge**
- **2A**: Implement instruction on recursion as a problem-solving technique, including its mathematical foundations (sequences vs. functions, calculus connections)
- **2C**: Teach dynamic programming and memoization as optimization strategies
- **2D**: Compare multiple algorithmic approaches to the same problem (iterative vs. recursive)

**Standard 3: Learning Environment**
- **3B**: Provide opportunities for students to engage in authentic problem-solving (Advent of Code context)
- **3C**: Facilitate student reflection on computational thinking practices (performance analysis, optimization)

**Standard 5: Professional Growth and Identity**
- **5A**: Stay current with discipline changes (2025 AP CSA curriculum revisions)
- **5B**: Advocate for comprehensive CS education (discussing why recursion remains important despite removal from AP CSA)

**Standard 4: Equity and Inclusion**
- **4C**: Prepare all students for post-secondary CS study, including content beyond the AP exam when pedagogically valuable

---

## Teaching Notes

### Introducing Recursion (For Teachers)

Even though recursion is removed from AP CSA, teachers may still want to cover it as enrichment or for college prep. Suggested progression:

1. **Start with mathematical examples**: Factorial, Fibonacci
2. **Show the base case / recursive case pattern**
3. **Visualize the call stack**: Draw the recursive calls as a tree
4. **Practice: Write simple recursive methods**: sum, power, digit sum
5. **Advanced: Tree/graph traversal**: Where recursion shines

### Common Student Misconceptions

1. **"Recursion is always slower"**
   - Counter-example: Our Part 2 results!
   - Truth: It depends on implementation details

2. **"I need to trace every recursive call"**
   - This leads to confusion with deep recursion
   - Better: Trust the base case, verify one level

3. **"Memoization is just caching"**
   - It's more: It transforms exponential to polynomial time
   - It's a algorithmic pattern, not just an optimization

### Extension Activities

1. **Fibonacci Challenge**: Implement naive recursive, memoized recursive, and iterative versions. Benchmark with F(40).

2. **Tree Visualization**: Use the beam splitter tree structure to visualize recursive calls

3. **Cache Hit Analysis**: Modify code to track cache hits vs. misses. How many recomputations did memoization save?

4. **Optimization Contest**: Challenge students to optimize the iterative Part 2. Can they beat 875 µs?

---

## Key Takeaways

1. **Recursion isn't gone from CS**: It's removed from AP CSA, but essential for college-level CS1
2. **Mathematical foundations**: Recursion = functional thinking, iteration = sequential thinking
3. **Memoization transforms impossible to tractable**: Exponential → polynomial time complexity
4. **Bottom-up DP dominates**: 10x faster than stack-based, 6x faster than memoization
5. **Classic algorithms are classic for a reason**: Simple array DP beats clever alternatives
6. **Cache-friendly code wins**: Sequential array access >> HashMap lookups
7. **Choose the right data structure**: HashMap vs. array = 10x performance difference
8. **Multiple solutions exist**: Compare and learn from different approaches

**Final Thought**: The removal of recursion from AP CSA doesn't diminish its importance in computer science. Students planning to major in CS should still learn recursive thinking - it's a fundamental problem-solving tool that appears throughout the discipline. This problem demonstrates why: some problems are naturally recursive, and fighting that structure makes code more complex, not simpler. But it also shows that when performance matters, understanding the fundamentals of how computers access memory (arrays vs. hash tables, cache locality, etc.) can make a dramatic difference - bottom-up DP isn't just elegant, it's **10x faster**.

---

## References and Further Reading

### Academic Sources
- Garcia, R., & Craig, M. (2025). 20 Years Later: A Replication Study on Teaching CS1 Concepts. *ACM Transactions on Computing Education*, 25(2), Article 22. https://doi.org/10.1145/3730405
  - Survey of 178 CS1 educators showing recursion remains a core but challenging topic in introductory programming courses

### Curriculum Resources
- College Board: [AP CSA Revisions for 2025-26](https://apcentral.collegeboard.org/courses/ap-computer-science-a/future-revisions)
- CSTA: [Standards for CS Teachers (2020)](https://csteachers.org/teacherstandards)

### Algorithms and Problem-Solving
- Introduction to Algorithms (CLRS), Chapter 15: Dynamic Programming
- Grokking Algorithms by Aditya Bhargava, Chapter 8: Memoization

**Problem Source**: Advent of Code 2025, Day 7
**Full Solution**: Available at [github.com/scerruti/AoC2025](https://github.com/scerruti/AoC2025)
