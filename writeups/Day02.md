---
layout: post
title: "Day 2: Gift Shop"
date: 2025-12-02
categories: [writeup]
tags: [AP-CSA, LO-1.3.C, LO-1.15.B, LO-2.3.A, LO-2.7.B, LO-2.8.A, LO-4.3.A, LO-4.9.A, string-manipulation, patterns, long-exception]
---

# Day 2: Gift Shop

## Problem Summary
Day 2 presents a set of numeric ranges. For each part, you must process all numbers in these ranges and apply a digit pattern rule:

- **Part 1:** Sum all numbers where the number has even length and the first half of its digits match the second half (e.g., 1212, 3434).
- **Part 2:** Sum all numbers where the number consists of repeated patterns of any length (e.g., 1212, 343434, 123123123).

## AP CSA Subset Compliance
- The solution is written to be as AP CSA compliant as possible.
- **Exception:** The input ranges contain numbers larger than the maximum value of `int` in Java (`2,147,483,647`). Therefore, `long` and `Long.parseLong` are used for parsing and iterating over these values. This is not part of the AP CSA Java subset, but is necessary for this problem's input.
- All other code constructs (loops, conditionals, `String`, `ArrayList`, `substring`, etc.) are AP CSA compliant.

## Solution Overview
- The input is a single line of comma-separated ranges (e.g., `"24-46,124420-259708,..."`).
- For each range, the code iterates from the lower to the upper bound (inclusive).
- Each number is converted to a string for digit pattern analysis.

### Part 1 Logic
- Skip numbers with odd length.
- For even-length numbers, compare the first half and second half digit-by-digit using `substring`.
- If all corresponding digits match, add the number to the sum.

### Part 2 Logic
- For each number, try all possible pattern lengths from 1 up to half the number's length.
- Only consider pattern lengths that divide the number's length evenly.
- Use `substring` to extract and compare patterns.
- If the number is made up of repeated patterns, add it to the sum and stop checking further pattern lengths for that number.

## AP CSA Learning Objectives
This solution addresses the following official AP Computer Science A Learning Objectives (2025):

- **LO 1.3.C:** Develop code for arithmetic expressions (`%`, `/`, `+=`).
- **LO 1.15.B:** Develop code to call methods on string objects (`substring`, `equals`, `length`, `valueOf`).
- **LO 2.3.A:** Develop code to represent branching logical processes (`if` statements).
- **LO 2.7.B:** Develop code to represent iterative processes using `while` loops (implicitly covered by `for` loop logic).
- **LO 2.8.A:** Develop code to represent iterative processes using `for` loops.
- **LO 4.3.A:** Develop code used to represent collections of related data using 1D array objects (`String[]` from `split`).
- **LO 4.9.A:** Develop code used to traverse the elements of an `ArrayList` (input traversal).

## Teaching Notes
- **String Manipulation:** This problem is an excellent exercise for practicing `substring` logic without relying on `charAt` (which is technically outside the subset, though widely taught).
- **Compliance Exception:** The use of `long` provides a teachable moment about integer overflow and data type limits, even though `long` is not on the exam.
- **Algorithm Efficiency:** Part 2 involves nested loops (checking every pattern length for every number), which is a good opportunity to discuss basic efficiency, although the input size for AoC usually forgives brute-force approaches in early days.

## Java Quick Reference Compliance
- `String` methods used: `substring`, `equals`, `length`.
- `ArrayList` methods used: `get`.
- `Integer`/`Long` parsing: `Long.parseLong` is used here, analogous to the compliant `Integer.parseInt`.

---
**Summary:** This writeup demonstrates AP CSA concepts in a practical context, with a clearly noted exception for `long` data types required by the puzzle input.