# Day 1: Secret Entrance

## Summary

The main challenge in Day 1 is handling wraparound arithmetic (modulo math) on a dial with 100 positions. Part 1 is intentionally solved without using modulo to demonstrate a naive approach, using while loops to “unwrap” the dial. Part 2 introduces a more efficient solution using division and modulo, and features an if-else if chain to handle multiple cases involving two variables.

All code is written to comply with the AP Computer Science A (AP CSA) Java subset. In particular, all input is provided as an `ArrayList<String>`, and only AP subset methods and types are used.

## Key Concepts

- Iteration (for-each and while loops)
- Selection (if, else if)
- String methods (`substring`, `equals`)
- Arithmetic (`%`, `/` and `Integer.parseInt`)
- Use of `ArrayList<String>` for input

## Approach

### Part 1: Naive Wraparound

In Part 1, the dial is advanced by a number of clicks, and the position is kept within [0, 99] by repeatedly adding or subtracting 100 using while loops. This approach avoids modulo and is useful for illustrating the underlying math. This is a good opportunity to discuss how to solve problems without relying on built-in operators.

```java
// Wrap position to [0, 99] without modulo
while (position < 0) {
    position += 100;
}
while (position >= 100) {
    position -= 100;
}
```

### Part 2: Efficient Modulo and Case Handling

Part 2 uses integer division and modulo to efficiently count full rotations and handle the remaining clicks. The if-else if chain is used to handle all possible cases for the dial’s position and its transition, based on both the current and previous position. This demonstrates how to combine arithmetic and selection to manage a range of values over two variables.

```java
// Count full rotations and keep only the remainder
count += clicks / 100;
clicks = clicks % 100;

// ...
if (position == 0) {
    count++;
} else if (position < 0 && oldPosition > 0) {
    position += 100;
    count++;
}
// ...other cases...
```

## AP CSA Subset Compliance and Learning Objectives

This solution is fully AP CSA subset compliant and addresses the following official AP Computer Science A Learning Objectives (2025):

- **LO 1.3.C:** Develop code for arithmetic expressions and determine the result of these expressions. (Used for integer division `/` and modulo `%`).
- **LO 1.15.B:** Develop code to call methods on string objects and determine the result of calling these methods. (Used for `substring` and `equals`).
- **LO 2.3.A:** Develop code to represent branching logical processes by using selection statements. (Used for the `if-else if` chains in Part 2).
- **LO 2.5.A:** Develop code to represent compound Boolean expressions. (Used for `&&` logic in conditions).
- **LO 2.7.B:** Develop code to represent iterative processes using `while` loops. (Used for the naive "unwrapping" in Part 1).
- **LO 4.7.A:** Develop code to use Integer and Double objects from their primitive counterparts. (Used for `Integer.parseInt`).
- **LO 4.8.A:** Develop code for collections of related objects using `ArrayList` objects.
- **LO 4.9.A:** Develop code used to traverse the elements of an `ArrayList`. (Used for the enhanced `for` loop).

## Teaching Notes

- **Comparison of Approaches:** This problem contrasts a naive `while` loop approach (Part 1) with a mathematical modulo approach (Part 2), offering a clear example of how algorithm selection impacts efficiency.
- **Compound Logic:** The `if-else if` chain in Part 2 demonstrates how to handle complex state transitions involving multiple variables, reinforcing **LO 2.5.A**.
- **AP Compliance:** `ArrayList<String>` is the standard data structure for this course. Additionally, `Integer.parseInt` is used to parse input; this method is explicitly listed in the **Java Quick Reference** (Appendix) of the *AP Computer Science A Course and Exam Description*, ensuring full compliance with exam standards.
