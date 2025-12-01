
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

This solution is fully compliant with the 2025 AP Computer Science A Java subset and directly addresses the following official Learning Objectives (LOs):

- **String Manipulation**
    - **LO 1.15.B:** Develop code to call methods on string objects and determine the result of calling these methods.  
        (Uses `substring` and `equals` to parse and compare input.)
- **Arithmetic**
    - **LO 1.3.C:** Develop code for arithmetic expressions and determine the result of these expressions.  
        (Uses arithmetic operators such as `%`, `/`, and `+=` for dial position calculations.)
- **Selection**
    - **LO 2.3.A:** Develop code to represent branching logical processes by using selection statements and determine the result of these processes.  
        (Uses `if`, `else if`, and `else` for control flow.)
    - **LO 2.5.A:** Develop code to represent compound Boolean expressions and determine the result of these expressions.  
        (Uses compound conditions with `&&`.)
- **Iteration**
    - **LO 2.7.B:** Develop code to represent iterative processes using while loops and determine the result of these processes.  
        (Uses `while` loops for wraparound logic.)
- **Data Collections**
    - **LO 4.8.A:** Develop code for collections of related objects using ArrayList objects and determine the result of calling methods on these objects.  
        (Uses `ArrayList<String>` for input.)
    - **LO 4.9.A:** Develop code used to traverse the elements of an ArrayList and determine the results of these traversals.  
        (Uses for-each loop to process input lines.)
- **Wrapper Classes**
    - **LO 4.7.A:** Develop code to use Integer and Double objects from their primitive counterparts and determine the result of using these objects.  
        (Uses `Integer.parseInt` to convert input strings to integers.)

All input is handled as `ArrayList<String>`, and only methods and classes listed in the Java Quick Reference are used. No Java features outside the AP subset are present.



## Teaching Notes

- This problem provides an opportunity to discuss both naive and efficient approaches to wraparound arithmetic, reinforcing the value of understanding underlying math and control flow.
- Students should first implement the while-loop version, then refactor to use modulo and division, highlighting the difference between iterative and arithmetic solutions.
- The use of `if`, `else if`, and compound Boolean expressions (`&&`) demonstrates branching logic and selection, as required by the AP subset.
- String parsing with `substring` and `equals` is fully compliant with LO 1.15.B.
- Input is managed with `ArrayList<String>` and traversed using a for-each loop, both of which are explicitly allowed and listed in the Java Quick Reference (see Appendix of the Course and Exam Description).
- The use of `Integer.parseInt` is compliant, as it is included in the Java Quick Reference and supports LO 4.7.A.


---
**Summary:** This writeup and solution are fully AP CSA compliant, use only allowed types and methods, and are designed for teaching and learning the AP Computer Science A Java subset.
