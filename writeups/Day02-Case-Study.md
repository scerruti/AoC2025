---
layout: post
title: "Case Study: Optimizing Algorithms in AP CSA (Day 2)"
date: 2025-12-02
categories: [case-study]
tags: [AP-CSA, CSTA-1e, CSTA-3, CSTA-5a, LO-1.15.B, LO-1.3.C, LO-2.7.B, LO-2.3.A, optimization, algorithms, performance, teaching, inquiry-based-learning]
---

# Case Study: Optimizing Algorithms in AP CSA (Advent of Code Day 2)

## 0. Context: The Teacher's Perspective

As a high school Computer Science teacher, I often initially attack problems the way my students would: with the tools currently in hand. When "all you have is a hammer," every problem looks like a nail. In the context of AP CSA, that "hammer" is often `String` manipulation and standard loops.

I also work under strict time constraints, aiming to understand and share solutions quickly enough to be useful to the community. Consequently, my first draft is rarely optimal. The real refinement often happens away from the keyboard—specifically, during my morning commute. I spend that time mentally iterating on the logic, knowing the brute-force approach is just a placeholder.

This case study documents that iterative process. It shows how voice conversations with AI (Gemini) were used to record those commuting thoughts, test the mental models, and rigorously refine the solution from a naive "student" approach to a mathematically optimized one.

Additionally, this journey reflects the importance of **community research** (Standard 3). After solidifying my initial approach, I turned to the Advent of Code subreddit to review how others tackled the problem. This step—checking my work against the broader developer community—was crucial. It not only validated my optimized logic but also exposed me to entirely different paradigms (like the "Inverse Strategy"), turning the daily puzzle into a deeper learning opportunity for myself and my students.

## 1. Summary of the Journey

We started with a standard string-based solution for detecting repeating patterns in a number (e.g., finding if `121212` is composed of repeating `12`s). Through analysis, we identified performance bottlenecks and mathematical properties that allow for significant optimization.

### Phase 1: The Naive String Approach
* **Concept:** Check every possible pattern length from `1` to `N/2` using String manipulation.
* **Mechanism:** `substring` extraction and comparison.
* **Performance:** $O(N^2)$ (Quadratic). ~450ms runtime.
* **Issues:** High memory overhead due to object allocation; redundant checks.

### Phase 2: Mathematical Pruning (The "Divisor" Strategy)
* **Concept:** A pattern of length $L$ can only repeat perfectly if $L$ is a divisor of the total string length.
* **Optimization:** Check only divisors.
* **Source:** Standard algorithmic optimization (Gemini/Stephen).

### Phase 3: The "Sieve" Logic (Failure Propagation)
* **Concept:** Using the result of one check to skip others.
* **Evolution:**
    * **Initial Idea (Stephen):** If a small pattern (e.g., length 2) fails, can we skip multiples (e.g., length 4)?
    * **Refinement (Gemini):** Strict sieving is unsafe because a larger pattern might not be a repetition of the smaller one.
    * **Correction (Stephen):** Pointed out that if the small pattern fails *after* matching a prefix, we can indeed prune multiples that fit within that prefix.
    * **Final Strategy:** Check divisors in **Descending Order**. If a large pattern fails, its factors *must* fail.
* **Result:** Pruning logic that eliminates redundant checks based on mathematical necessity.

### Phase 4: Pure Math (The "No-String" Optimization)
* **Concept:** Replacing String objects with primitive arithmetic to eliminate allocation overhead.
* **Mechanism:** Use integer division (`/`) and modulo (`%`) to extract digits.
* **Implementation (Gemini):** Provided the `PatternMatcher` class using `Math.pow` and later a static lookup table.
* **Performance:** **1ms runtime**. $O(1)$ Space Complexity.

### Phase 5: The Inverse Strategy (Generating vs. Checking)
* **Concept:** Instead of checking if a number has a pattern, generate all valid patterns and check if they fall in the range.
* **Source (Reddit via Stephen):** Identified as a community solution for large inputs.
* **Implementation (Gemini):** Provided `InvalidIdCalculator` to mathematically generate patterned numbers.
* **Benefit:** Performance depends on the number of *valid solutions* (sparse), not the range size (massive).

## 2. Pedagogical & Standards Alignment

This case study serves as a resource for teachers to model the "expert learner" process. It directly aligns with the **CSTA Standards for CS Teachers**, illustrating how a teacher's own professional growth translates into better instructional design.

### Alignment with CSTA Teacher Standards

* **Standard 1e: Develop programs and interpret algorithms.**
    * *Standard:* "Design, implement, debug, and review programs in an iterative process... explain tradeoffs associated with different algorithms."
    * *Application:* This case study moves beyond simply "getting the right answer." It explicitly models the trade-offs between **Readability** (Phase 1: Strings), **Memory Efficiency** (Phase 4: Primitives), and **Algorithmic Complexity** (Phase 5: Generators). Sharing this journey allows students to see that "correctness" is just the first step in software engineering.

* **Standard 3: Professional Growth and Identity.**
    * *Standard:* "Model continuous learning... participate in CS professional learning communities."
    * *Application:* The inclusion of the "Reddit Research" phase demonstrates that teachers are also learners. By validating solutions against the broader CS community and refining them with tools like AI, we model how professionals stay current and improve their craft.

* **Standard 5a: Use inquiry to facilitate student learning.**
    * *Standard:* "Guide student learning through asking key questions rather than offering solutions..."
    * *Application:* Instead of presenting the optimized "Pure Math" solution immediately, this case study provides a roadmap for **Inquiry-Based Learning**.
        * *Prompt:* "Why does our String solution get slower as the numbers get bigger?" (Leading to Phase 2).
        * *Prompt:* "If a number isn't made of '1212', can it be made of '12'?" (Leading to Phase 3 Logic).
        * *Prompt:* "Can we check digits without turning them into text?" (Leading to Phase 4).

## 3. Teaching Notes & LO Mapping

| Concept | AP CSA Learning Objective | Teaching Opportunity |
| :--- | :--- | :--- |
| **Substring Parsing** | **LO 1.15.B** (String Methods) | Discussing object immutability and heap allocation cost (why `substring` creates "garbage"). |
| **Modulo Check** | **LO 1.3.C** (Arithmetic) | Using `%` to determine divisors vs. mere remainders; reinforcing place value logic. |
| **Nested Iteration** | **LO 2.7.B** (Loops) | Analyzing Big-O. Comparing the $O(N^2)$ brute force check vs. the $O(1)$ lookup table. |
| **Logical Pruning** | **LO 2.3.A** (Selection) | Using the contrapositive logic ("If P implies Q, then Not Q implies Not P") to skip code execution. |
| **Algorithm Design** | **LO 1.B** (Computational Thinking) | Comparing "Check All" vs "Generate Valid" strategies—a key insight for advanced problem solving. |

**Final Takeaway for Teachers:**
This exercise validates that even simple problems (like Advent of Code Day 2) contain deep reservoirs of Computer Science concepts. By documenting the optimization process—flaws and all—we provide students with a realistic view of how code evolves from "working" to "excellent."