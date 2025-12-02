---
layout: post
title: "Case Study: From Naive Loops to Infinite Tapes (Day 1)"
date: 2025-12-01
categories: [case-study]
tags: [AP-CSA, optimization, refactoring, teaching, modulo]
---

# Case Study: From Naive Loops to Infinite Tapes (AoC 2025 Day 1)

## 0. Context: The Teacher's Perspective

This solution was developed under the constraints of a real school day. I started this session in my school account, refining the solution iteratively with Gemini. As a high school CS teacher, I often work quickly, refining algorithms in my head during my morning commute. I use voice conversations with AI to capture these "commute thoughts" and test them against the strict constraints of the AP CSA curriculum.

This case study is not just "Bad Code vs. Good Code." It highlights the gap between **"What I Teach"** (the AP CSA Subset) and **"What I Wish I Taught"** (Professional Domain Modeling), and maps the specific refactoring steps that bridge that gap.

## 1. What I Teach: The AP CSA Subset (The Starting Point)

In the classroom, our primary goal is correctness within the constraints of the AP Java Subset. We focus on imperative logic and explicit state modification.

### The "Manual Wrap" Approach
Students intuitively treat the dial as a physical object. If it goes past 99, they "reset" it. If it goes below 0, they "wind it back."

**The Code (AP CSA Compliant):**
```java
// "Unwrapping" the dial using while loops (LO 2.7.B)
while (position < 0) {
    position += 100;
}
while (position >= 100) {
    position -= 100;
}
```

* **Why we teach this:** It is visually intuitive. It reinforces **LO 2.7.B (Iteration)** and **LO 2.3.A (Selection)**. It handles the edge cases without requiring complex math logic that students might struggle with under pressure.
* **The Limitation:** It represents "Simulation" thinking rather than "Mathematical" thinking. It is $O(k)$ where $k$ is the number of wraps, rather than $O(1)$.

## 2. The Refactoring Journey (The Bridge)

This section documents the specific "teachable moments" that occurred as we moved from the naive solution toward a robust one.

### Phase A: The "Modulo Trap"
We attempted to optimize the `while` loops using the `%` operator.
* **The Discovery:** In Java, `-10 % 100` returns `-10`, not `90`. This is a classic "gotcha" for students moving from math class to coding.
* **The Fix:** We introduced `Math.floorMod(position, 100)` (or the formula `(a % b + b) % b`) to achieve true mathematical modulus.

### Phase B: Semantic Naming
We moved from generic variable names to domain-specific terms to clarify intent.
* `dir` ("L"/"R") $\rightarrow$ `multiplier` (1/-1).
* `clicks` $\rightarrow$ `delta` (change in position).
* **Pedagogical Value:** This shifts the student's mental model from "following instructions" to "modeling physics."

### Phase C: Part 2 Logic (Simulation vs. Calculation)
Part 2 requires counting how many times the dial crosses zero.
* **Student Approach:** Simulate every single click in a loop to catch the crossing.
* **Refined Approach:** Calculate "Epochs."
    * We model the dial as an **Infinite Unrolled Tape**.
    * `startEpoch` = `Math.floorDiv(currentPos, 100)`
    * `endEpoch` = `Math.floorDiv(targetPos, 100)`
    * This converts a simulation problem ($O(N)$) into a math problem ($O(1)$).

## 3. What I Wish I Taught: The Aspirational Target

This is the destination. It uses Modern Java features (Records, Switch Expressions) and Stream processing. While outside the AP Subset, this is the code I want my students to eventually be able to write.

### The Source of Truth: `DialState`
Instead of loose variables (`int position`, `long count`), we encapsulate the entire state of the system in an immutable Record.

**Target Java Code (Modern/Stream):**
```java
record DialState(
    int position,       // Strictly 0-99
    long crossingCount, // Part 2 Answer
    int buffer,         // The number currently being parsed
    int multiplier,     // 1 or -1
    boolean parsing
) {
    private static final int DIAL_SIZE = 100;

    DialState accept(int charCode) {
        char c = (char) charCode;
        return switch (c) {
            case 'R' -> new DialState(position, crossingCount, buffer, 1, parsing);
            case 'L' -> new DialState(position, crossingCount, buffer, -1, parsing);
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> 
                new DialState(position, crossingCount, buffer * 10 + (c - '0'), multiplier, true);
            default -> {
                if (!parsing) yield this;
                
                // The "Aspirational" Logic: Infinite Tape Physics
                long delta = (long) multiplier * buffer;
                long startEpoch = Math.floorDiv(position, DIAL_SIZE);
                long endEpoch   = Math.floorDiv(position + delta, DIAL_SIZE);
                
                yield new DialState(
                    Math.floorMod(position + delta, DIAL_SIZE), 
                    crossingCount + Math.abs(endEpoch - startEpoch), 
                    0, 1, false
                );
            }
        };
    }
}
```

## 4. Pedagogical & Standards Alignment

### Alignment with CSTA Teacher Standards

* **Standard 1a (Content Knowledge):** "Apply CS practices... Developing and Using Abstractions."
    * *Application:* Moving from the "Manual Wrap" loop to the `DialState` record demonstrates how abstraction simplifies complex state management.
* **Standard 1e (Iterative Design):** "Design, implement, debug, and review programs..."
    * *Application:* This case study explicitly models the *review* phase—taking a working solution and refining it for correctness (Modulo Trap) and readability (Naming).
* **Standard 3 (Professional Growth):** "Participate in CS professional learning communities."
    * *Application:* By documenting this journey and sharing the "Aspirational" code, we provide a path for other teachers to deepen their own content knowledge beyond the AP subset.

### Teaching Notes for AP CSA

| Concept | "What I Teach" (Subset) | "What I Wish I Taught" | Learning Objective |
| :--- | :--- | :--- | :--- |
| **Logic** | `if/else` chains | Switch Expressions | **LO 2.3.A** (Selection) |
| **Data** | Mutable `int` variables | Immutable `record` | **LO 5.A** (Data Encapsulation) |
| **Math** | `while` loop unwrapping | `Math.floorDiv` / Epochs | **LO 1.3.C** (Arithmetic) |
| **Input** | `Scanner` / `split` | Stream / State Machine | **LO 4.3.A** (Processing Data) |