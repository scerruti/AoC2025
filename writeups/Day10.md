---
layout: post
title: "Day 10: Factory"
date: 2025-12-10
categories: [writeup]
tags: [AP-CSA, CSTA, CSTA-1A-AP-11, CSTA-2-AP-13, CSTA-2-AP-14, beyond-subset, BFS, recursion, memoization, state management, optimization, VAR-1.A, MOD-2.A, CON-2.A, VAR-2.E, VAR-2.F, AAP-1.A]
draft: false
---

# Day 10 Writeup

> **Note:** This writeup was generated with assistance from GitHub Copilot to document the solution and pedagogical approach.

## Summary

Day 10 involved initializing factory machines with indicator lights and buttons that toggle specific lights. Part 1 required finding the minimum button presses to match a target light configuration for each machine.

The problem highlighted the importance of state space management and exploring alternative algorithms when brute force fails.

## Key Concepts

- **Breadth-First Search (BFS):** Used for Part 1 to explore button press sequences level by level, ensuring the shortest path.
- **State Representation:** For Part 1, boolean arrays for light states; for Part 2, integer arrays for joltage counters.
- **Cycle Detection:** Preventing infinite loops by tracking visited states in BFS.
- **Recursive Backtracking with Memoization:** Attempted for Part 2 to prune invalid paths and cache results, but combinatorial explosion overwhelmed memory.
- **Integer Linear Programming (ILP):** Modeled Part 2 as an optimization problem with variables for button presses, constraints for joltage sums, and minimization of total presses.
- **Pruning and Optimization:** Reducing state space by skipping invalid states (e.g., over-joltage) and using efficient data structures like HashSets for visited states.

## Approach

### Part 1: Minimum Button Presses for Light Configurations

The input consists of lines like `[#.#] A(0,2) B(0,1)`, where the brackets show the target light state (# on, . off), and buttons specify which lights they toggle.

I parsed the target state into a boolean array and buttons into objects with light indices.

For BFS, I used a queue of lists of ButtonPress objects, each tracking the button and resulting state. A visited set prevented cycles by storing unique (button, state) combinations.

The algorithm:
- Start with single-button presses.
- If a press matches the target, record the depth.
- Otherwise, enqueue extensions with additional presses.
- Continue until a solution is found.

This worked for Part 1, finding minimum presses efficiently.

### Button Class
```java
private class Button {
    int[] lightNumbers;
    public Button(String buttonString) {
        // Parse buttonString like "A(0,2)" to get indices
    }
    public boolean[] press(boolean[] lightStates) {
        // Toggle lights
    }
    // equals and hashCode for cycle detection
}
```

### BFS for Part 1
```java
ArrayList<ArrayList<ButtonPress>> buttonPresses = new ArrayList<>();
// Initialize with single presses
while (buttonPresses.size() > 0) {
    // Dequeue, check if target, enqueue extensions
}
```

### Part 2: Minimum Button Presses for Joltage Counters

Part 2 shifted to joltage counters that buttons increment, aiming for exact target values. I started with BFS for both parts but encountered performance issues in Part 2, leading to recursive backtracking with memoization, which still caused OOM errors. Finally, I switched to Integer Linear Programming (ILP) using the ojAlgo library for an optimal solution.

Part 2 changed to joltage counters starting at 0, buttons incrementing specific counters, goal to match exact target values like `{1,2,0,3}`.

Initial BFS on joltage states was too slow due to large state space.

I tried recursive backtracking:
- Find the counter with the fewest remaining clicks.
- Generate combinations of button presses that achieve that.
- Recurse on reduced state, memoizing results.
- But findCombinations exploded combinatorially, causing OOM.

Finally, ILP:
- Variables: x_b (press count for button b, integer >=0).
- Constraints: For each counter i, sum x_b over buttons affecting i = target[i].
- Objective: Minimize sum x_b.
- Used ojAlgo library to solve.

This provided optimal solutions without performance issues.

## Code Snippets

### ILP for Part 2
```java
ExpressionsBasedModel model = new ExpressionsBasedModel();
Variable[] vars = new Variable[buttons.length];
for (int i = 0; i < buttons.length; i++) {
    vars[i] = model.addVariable("x" + i).lower(0).integer(true);
}
// Add constraints and objective
Optimisation.Result solveResult = model.minimise();
```

## Lessons Learned

- **State Space Explosion:** BFS works for small states but fails for large ones; memoization helps but not always enough.
- **Algorithm Adaptation:** When one approach fails, consider mathematical modeling like ILP for optimization problems (note: ILP is an advanced technique beyond AP CSA scope).
- **Debugging Incrementally:** Small errors (e.g., indexing, target parsing) cost time; test early and often.
- **Library Usage:** ojAlgo simplified ILP implementation; Maven dependencies are powerful.
- **Pruning Importance:** Skip invalid states early to reduce computation.
- **AP CSA Relevance:** Arrays for states, recursion for backtracking, optimization for efficiency.

## AP CSA Learning Objectives

- **VAR-1.A:** Represent collections of related primitive or object reference data using one-dimensional (1D) arrays (boolean and int arrays for light states and joltage counters).
- **MOD-2.A:** Create class instances using constructors (Button and ButtonPress classes).
- **CON-2.A:** Represent iterative processes using a for loop (BFS traversal and recursive calls).
- **VAR-2.E:** Represent collections of object reference data using ArrayList objects (for queues in BFS).
- **VAR-2.F:** Represent collections of object reference data using HashSet objects (for visited states; note: HashSet is not in AP CSA subset).
- **AAP-1.A:** Select and call library methods to accomplish specific tasks (using ojAlgo for ILP, though beyond AP CSA).

## CSTA Standards for CS Teachers

This writeup aligns with the **CSTA Standards for CS Teachers (2023)**:

**Standard 1: Computational Thinking** - Teachers use computational thinking to understand and teach computer science.
- **1A-AP-11:** Decompose problems into smaller, manageable subproblems (breaking down button press sequences and state management).
- **1A-AP-12:** Abstraction in managing complexity (using classes for buttons and states).

**Standard 2: Algorithms and Programming** - Teachers understand algorithms and programming concepts.
- **2-AP-13:** Use and adapt classic algorithms (BFS for shortest path, recursive backtracking).
- **2-AP-14:** Use data structures (ArrayList for queues, HashSet for visited states).

**Standard 3: Programming Languages and Paradigms** - Teachers understand programming languages and paradigms.
- **3A-AP-17:** Create algorithms for mathematical and real-world problems (optimization with ILP modeling).

**Standard 4: Instructional Design** - Teachers design learning activities that support CS learning.
- **4a:** Learning Activities - Demonstrates problem-solving process from initial attempts to optimized solutions.
- **4b:** Differentiation - Provides scaffolded approaches from simple BFS to advanced ILP.

**Standard 5: Classroom Practice** - Teachers create inclusive learning environments.
- **5a:** Pedagogy - Uses inquiry-based learning through debugging and algorithm adaptation.
