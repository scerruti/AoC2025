---
layout: post
title: "Day 3: Lobby"
date: 2025-12-03
categories: [writeup]
tags: [AP-CSA, LO-1.3.C, LO-1.15.B, LO-2.3.A, LO-2.7.B, LO-2.8.A, LO-4.1.A, LO-4.2.A, LO-4.9.A, greedy-algorithm, arrays, string-manipulation, subsequences]
---

# Day 3: Lobby

> **Note:** This writeup was generated with assistance from GitHub Copilot (Claude Sonnet 4.5) to document the solution and pedagogical approach.

## Problem Summary

Day 3 involves processing strings of digits representing batteries in a battery bank. The goal is to select batteries that produce the maximum voltage when read as a multi-digit number:

- **Part 1:** Select exactly 2 batteries (digits) from each bank that form the largest 2-digit number when read in order.
- **Part 2:** Select exactly $k = 12$ batteries (digits) from each bank that form the largest $k$-digit number while maintaining their original left-to-right order.

The key constraint is that you cannot rearrange the batteries—you can only choose which ones to keep or skip, and the selected batteries must remain in their original sequence. Formally, given a sequence $S = d_1d_2...d_n$ of $n$ digits, find a subsequence of length $k$ that maximizes the resulting decimal number.

## AP CSA Subset Compliance

This solution is fully AP CSA subset compliant with one notable exception:

- **Exception:** Part 2 requires `long` instead of `int` because the resulting 12-digit number exceeds the maximum value of `int` (2,147,483,647). While `long` is not part of the AP CSA Java subset, it is necessary for this problem.
- All other constructs (loops, conditionals, arrays, `String` methods, `ArrayList`) are AP CSA compliant.

## Solution Overview

### Part 1: Finding the Best 2-Digit Number

The approach iterates through the battery string, tracking potential "tens" and "ones" digits:

1. Start with the first two digits as initial candidates.
2. For each subsequent digit:
   - Check if the previous digit is larger than the current "tens" digit.
   - If so, update both "tens" (to the previous digit) and "ones" (to the current digit).
   - Otherwise, check if the current digit is larger than "ones" and update if needed.
3. Return the 2-digit number formed by $10 \times \text{tens} + \text{ones}$.

This greedy approach ensures we always capture the largest possible consecutive pair.

```java
public int maxJoltage() {
    int tens = Integer.parseInt(batteries.substring(0,1));
    int ones = Integer.parseInt(batteries.substring(1,2));
    int second = ones;

    for (int i = 2; i < batteries.length(); i++) {
        int first = second;
        second = Integer.parseInt(batteries.substring(i, i+1));
        if (first > tens) {
            tens = first;
            ones = second;
        } else {
            if (second > ones) {
                ones = second;
            }
        }
    }
    return tens*10 + ones;
}
```

### Part 2: Greedy Subsequence Selection

Part 2 uses a greedy algorithm to select the largest subsequence of $k = 12$ digits:

1. Track how many digits we've selected (`selectedCount`) and how many we can still skip (`toSkip = n - k`).
2. For each digit $d_i$ in the original string:
   - **Greedy decision:** If $d_i$ is larger than previously selected digits, and we still have skips available, remove those smaller digits and prepare to add $d_i$.
   - Add $d_i$ if we haven't selected $k$ digits yet.
   - Otherwise, skip $d_i$.
3. Build the final number using $\text{result} = \sum_{i=0}^{k-1} \text{selected}_i \times 10^{k-1-i}$.

This algorithm ensures that at each step, we maintain the largest possible leading digits.

```java
public long maxJoltage(int numBatteries) {
    int[] selectedBatteries = new int[numBatteries];
    int selectedCount = 0;
    int toSkip = batteries.length() - numBatteries;
    
    for (int i = 0; i < batteries.length(); i++) {
        int currentDigit = Integer.parseInt(batteries.substring(i, i + 1));
        
        // Remove previously selected digits if current is better
        while (selectedCount > 0 && 
               toSkip > 0 && 
               selectedBatteries[selectedCount - 1] < currentDigit) {
            selectedCount--;
            toSkip--;
        }
        
        // Add current digit if we haven't selected enough yet
        if (selectedCount < numBatteries) {
            selectedBatteries[selectedCount] = currentDigit;
            selectedCount++;
        } else {
            toSkip--;
        }
    }
    
    long joltage = 0;
    for (int i = 0; i < numBatteries; i++) {
        joltage = 10 * joltage + selectedBatteries[i];
    }
    return joltage;
}
```

## AP CSA Learning Objectives

This solution addresses the following official AP Computer Science A Learning Objectives (2025):

- **LO 1.3.C:** Develop code for arithmetic expressions (`*`, `+`, `-`, arithmetic with array indices).
- **LO 1.15.B:** Develop code to call methods on string objects (`substring`, `length`).
- **LO 2.3.A:** Develop code to represent branching logical processes (`if`, `else` statements).
- **LO 2.7.B:** Develop code to represent iterative processes using `while` loops (used in the greedy removal step).
- **LO 2.8.A:** Develop code to represent iterative processes using `for` loops.
- **LO 4.1.A:** Develop code to represent collections of related primitive data using 1D array objects (`int[]`).
- **LO 4.2.A:** Develop code to traverse 1D array objects using iteration statements.
- **LO 4.9.A:** Develop code used to traverse the elements of an `ArrayList` (input traversal).

## Teaching Notes

- **Greedy Algorithms:** This problem introduces a classic greedy algorithm pattern—making locally optimal choices at each step to achieve a globally optimal solution. Part 2 is particularly instructive as it demonstrates how to "backtrack" by removing previously selected elements when better options appear.

- **Array Manipulation:** The solution uses an array to store selected digits and dynamically adjusts `selectedCount` to simulate adding and removing elements, which is a useful technique when working within AP CSA constraints (no `Stack` or `ArrayList` removal).

- **String to Digit Conversion:** The use of `Integer.parseInt(batteries.substring(i, i+1))` reinforces string manipulation and parsing techniques, both important AP CSA skills.

- **Compliance Exception:** The use of `long` in Part 2 provides an opportunity to discuss integer limits and overflow, even though `long` is not on the exam. Students should understand why `int` is insufficient for this problem.

- **Refactoring for AP CSA:** The original solution used an inner `Bank` class (not AP compliant). The code was refactored to use a separate `Bank` class with static methods, which is AP subset compliant and demonstrates proper separation of concerns.

## Algorithm Complexity

- **Part 1:** $O(n)$ where $n$ is the length of the battery string—single pass through the digits.
- **Part 2:** $O(n \times k)$ in the worst case, where $n$ is the string length and $k$ is the number of batteries to select. The `while` loop inside the `for` loop can remove up to $k$ elements total across all iterations, making the overall complexity linear in practice: $O(n + k) = O(n)$ since $k \leq n$.

---

**Summary:** Day 3 demonstrates greedy algorithm design, array manipulation, and the practical application of maintaining ordered subsequences—all within AP CSA compliance (except for the necessary use of `long` in Part 2).
