
# Day 1: Dial Simulation

## Summary


The main challenge in Day 1 is handling wraparound arithmetic (modulo math) on a dial with 100 positions. Part 1 is intentionally solved without using modulo to demonstrate a naive approach, using while loops to “unwrap” the dial. Part 2 introduces a more efficient solution using division and modulo, and features an if-else if chain to handle multiple cases involving two variables.

All code is written to comply with the AP Computer Science A (AP CSA) Java subset. In particular, all input is provided as an `ArrayList<String>`, and only AP subset methods and types are used.

## Key Concepts

- Iteration (for-each loop)
- Selection (if, else if)
- String methods (`substring`, `equals`)
- Modulo arithmetic
- Use of `ArrayList<String>` for input (AP CSA compliant)

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

- **LO 3.1.1:** Evaluate Boolean expressions (used in if/else conditions)
- **LO 3.2.1:** Write iteration statements to traverse a String or array (for-each and while loops)
- **LO 3.3.1:** Write conditional statements (if, if-else, else-if)
- **LO 3.4.1:** Call and use String methods (`substring`, `equals`)
- **LO 3.5.1:** Perform arithmetic operations and type casting (integer math, modulo, `Integer.parseInt`)

All input is handled as `ArrayList<String>`, which is explicitly allowed in the AP subset for 2025. No Java features outside the AP subset are used.


## Teaching Notes

- This problem is a great opportunity to discuss the difference between naive and efficient approaches to wraparound arithmetic.
- Have students first implement the while-loop version, then refactor to use modulo and division. This reinforces the value of both approaches and the underlying math.
- The if-else if chain in Part 2 is a strong example for discussing multi-branch selection and how to reason about transitions between states.
- Emphasize the use of `ArrayList<String>` for AP CSA compliance. `Integer.parseInt` is included in the AP CSA subset and can be used directly.


---
**Summary:** This writeup and solution are fully AP CSA compliant, use only allowed types and methods, and are designed for teaching and learning the AP Computer Science A Java subset.
