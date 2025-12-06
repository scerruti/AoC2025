---
layout: post
title: "Day 6: Trash Compactor"
date: 2025-12-06
categories: [writeup]
tags: [AP-CSA, CSTA, MOD-1.C, MOD-1.E, CON-2.A.1, CON-2.B, VAR-2.E.1, VAR-2.E.2, VAR-2.F, Scanner, 2D-arrays, identity-values, parsing, nested-loops]
---

# Day 6: The Trash Compactor Math Puzzle

## A Story of Scanner, 2D Arrays, and Cross-Species Communication

### The Setup: Math Under Pressure

Luke Skywalker and Chewbacca are trapped in the Death Star's trash compactor. The walls are closing in, the dianoga has just released Luke's leg, and time is running out. Among the floating debris, Chewie spots something glowing—a waterproof datapad displaying what appears to be a math worksheet.

But this isn't just any worksheet. It's written in the format used by a cephalopod species (think octopus-like aliens with their own way of organizing information). Attached to it is a mantis shrimp merchant's bill—mantis shrimp being one of the galaxy's most successful traders, known for their ability to perceive wavelengths of light that other species can't even imagine.

The control panel next to them shows two input slots. Luke realizes they need TWO override codes: one from reading the worksheet in Basic (the common language, left-to-right), and one from reading it the way the cephalopods do.

Chewie roars in frustration—he's better at pulling people's arms out of their sockets than parsing multi-species data formats. Luke takes a deep breath and starts analyzing the problem systematically.

**The Challenge**: Same visual data, different reading patterns, completely different mathematical results.

### Part 1: Reading Like a Human (or Protocol Droid)

#### The Problem Domain

The worksheet shows math problems arranged in vertical columns:

```
123 328
 45  64
  6  98
 *   +
```

Reading left-to-right (human style), this represents two problems:
- Problem 1: `123 * 45 * 6 = 33,210`
- Problem 2: `328 + 64 + 98 = 490`
- Grand Total: `33,700`

Each column is a separate problem. Numbers are stacked vertically, with the operator at the bottom. The final answer is the sum of all problem results.

#### Tool #1: The Scanner Class

Luke's first challenge: the datapad's display is water-damaged, with inconsistent spacing. Some gaps between numbers are single spaces, others are multiple spaces. This is where the `Scanner` class becomes invaluable.

**Why Scanner Instead of String Methods?**

Consider the alternatives:
```java
// The brittle approach (what beginners often try):
String[] numbers = line.split(" ");  // Fails with multiple spaces!
// Result: ["123", "", "", "328"] - empty strings cause parseInt() to crash

// The complex approach:
String[] numbers = line.replaceAll("\\s+", " ").trim().split(" ");
// Works, but requires understanding regex (not AP CSA)
```

**The Scanner approach (AP CSA compliant):**
```java
Scanner lineScanner = new Scanner(input.get(row));
int problemIndex = 0;
while (lineScanner.hasNextLong()) {
    problems[problemIndex][row] = lineScanner.nextLong();
    problemIndex++;
}
lineScanner.close();
```

Like Chewie navigating an asteroid field, Scanner just *handles* the irregularities without overthinking. It automatically:
- Skips any amount of whitespace
- Recognizes valid numbers
- Handles edge cases gracefully
- Doesn't require regex knowledge

**Key Teaching Moment**: Scanner treats whitespace as a delimiter by default, making it perfect for parsing data with inconsistent spacing. It's a whitespace-tolerant parser—exactly what you need for messy real-world data.

#### Tool #2: The 2D Array

Luke needs to organize the data before processing it. He uses a 2D array where:
- Each **column** represents one complete problem
- Each **row** represents a position in that problem

```java
long[][] problems = new long[problemCount][rowCount];
```

**Array organization: `problems[problemIndex][rowIndex]`**

For our example:
```
problems[0][0] = 123    problems[1][0] = 328
problems[0][1] = 45     problems[1][1] = 64
problems[0][2] = 6      problems[1][2] = 98
```

**Why this organization?** Because we process *by problem*, not by row. We need to keep all numbers for one problem together, then calculate its result before moving to the next problem.

**Key Teaching Moment**: How you structure your data determines how easily you can process it. This array organization mirrors the problem domain—each problem is a unit of work.

#### Tool #3: Identity Values

The mantis shrimp merchant's bill provides a clue. In their 16-color-receptor vision, they understand that different operations need different starting points.

```java
// Initialize result with identity value: 1 for multiplication, 0 for addition
long result = operators[problemIndex].equals(MULTIPLY) ? 1 : 0;
```

**Why these starting values?**
- **Multiplication identity**: `1 × anything = anything`
- **Addition identity**: `0 + anything = anything`

Starting with the wrong identity gives wrong results:
- Start multiplication at 0? Result is always 0!
- Start addition at 1? Every answer is 1 too high!

**Key Teaching Moment**: Identity values are foundational in mathematics and programming. They're the "do nothing" values that let you start accumulating results correctly.

#### The Complete Algorithm

1. **Count** problems using Scanner (handles irregular spacing)
2. **Organize** data into a 2D array (problems by numbers)
3. **Process** each problem:
   - Start with the identity value for the operation
   - Apply the operation to each number
   - Add result to grand total
4. **Return** the grand total

```java
for (int problemIndex = 0; problemIndex < problemCount; problemIndex++) {
    long result = operators[problemIndex].equals(MULTIPLY) ? 1 : 0;
    
    for (int numberIndex = 0; numberIndex < rowCount; numberIndex++) {
        if (operators[problemIndex].equals(MULTIPLY)) {
            result *= problems[problemIndex][numberIndex];
        } else {
            result += problems[problemIndex][numberIndex];
        }
    }
    grandTotal += result;
}
```

**AP CSA Connections**:
- **Scanner class** (Unit 2: Using Objects)
- **2D arrays** (Unit 8: 2D Array)
- **Nested loops** (Unit 4: Iteration)
- **Conditional logic** (Unit 3: Boolean Expressions and if Statements)
- **Ternary operator** (Unit 3: Boolean Expressions)

**AP CSA Learning Objectives:**
- **MOD-1.C**: Call methods on objects (Scanner methods: `hasNextLong()`, `nextLong()`, `next()`)
- **MOD-1.E**: Evaluate expressions using relational operators (`==`, `!=`)
- **CON-2.A.1**: Use conditional statements (`if`, `else`, ternary operator)
- **CON-2.B**: Represent iterative processes using loops (nested `for` loops, `while` loops)
- **VAR-2.E.1**: Create and use 2D arrays
- **VAR-2.E.2**: Traverse 2D arrays using nested loops
- **VAR-2.F**: Work with wrapper classes (`Integer.parseInt()`)

**CSTA Standards for CS Teachers:**
- **2A**: Plan and teach learning experiences that advance student knowledge of programming concepts
- **2B**: Demonstrate knowledge and use of effective instructional strategies appropriate to the discipline (Scanner vs. String manipulation comparison for pedagogical clarity)
- **2C**: Plan and teach a lesson that develops students' ability to test and refine computational artifacts (three-phase parsing: count, parse, compute)
- **3A**: Design and teach appropriate assessments to measure student learning (identity values as conceptual understanding check)
- **5B**: Model inclusive and equitable practices in the design and implementation of learning experiences (cross-species communication metaphor for diverse perspectives)

### Part 2: The Cephalopod Perspective

Luke enters the first code. The panel beeps—one down, one to go. But how do cephalopods read this data?

Chewbacca points at the mantis shrimp merchant's signature. Luke remembers reading about mantis shrimp vision: they have 12-16 color receptors (humans have 3), can see polarized and ultraviolet light, and process visual information in ways we literally cannot perceive. Their neural structure is completely different from mammalian brains.

The same is true for cephalopods. Their distributed nervous system and unique eye structure means they process spatial information differently. Where humans read left-to-right, cephalopods read **right-to-left within each column space**, and they parse **digit positions** within those columns.

Looking at the same visual data:
```
123 328
 45  64
  6  98
*   +
```

A cephalopod sees different numbers because:
- Spaces within columns are **significant** (they indicate digit positions)
- Reading happens right-to-left within the column boundaries
- Each digit position builds a separate number

**Result**: Completely different numbers, completely different answer!

**Why is this beyond AP CSA (and AP CSP)?**

Part 2 requires:
- Tracking column boundaries with precise position indices
- Preserving spaces as meaningful data (not just delimiters)
- Building numbers by digit position across multiple rows
- Right-to-left processing with position arithmetic

This demonstrates **advanced algorithmic problem-solving** that would be challenging even for strong CS students. It's the kind of problem you'd encounter in competitive programming, technical interviews, or college-level data structures courses—not introductory AP Computer Science classes.

**Key Teaching Moment**: Same problem domain, radically different parsing strategies. What you see depends on how you're wired to see it. This is valuable to *discuss conceptually* (how different perspectives change data interpretation) even though the implementation details are beyond AP scope. Understanding your "user" (or reader, or data consumer) is crucial in computer science.

### The Escape: Two Codes, One Solution

Luke enters both codes. The trash compactor shudders and stops with a hiss of hydraulics. The walls are inches away from crushing them.

Chewie roars in triumph and pats Luke on the back (nearly knocking him into the garbage water). As they climb out through the maintenance hatch, Luke reflects on what he learned:

1. **Scanner is powerful**: It handles messy, real-world data better than string manipulation
2. **Data structure matters**: Organizing data to match your problem domain makes processing easier
3. **Identity values are fundamental**: Mathematical foundations matter in programming
4. **Perspective changes everything**: Different intelligences process the same information in different ways
5. **Always bring a Wookie**: For moral support and reaching high shelves

### Pedagogical Notes for AP CSA Teachers

**Part 1 is ideal for teaching:**

1. **Scanner as an alternative to String methods**
   - More robust than `split()` for whitespace-separated data
   - Demonstrates type-safe parsing (`hasNextLong()`, `nextLong()`)
   - Part of Java's standard library (AP CSA subset)

2. **2D array organization strategies**
   - Decision making: What does each dimension represent?
   - Matching data structure to problem domain
   - Iterating through 2D arrays with nested loops

3. **Identity values in mathematics**
   - Connection between math and programming
   - Why starting values matter
   - Accumulator patterns with different operations

4. **Code organization and readability**
   - Descriptive variable names from the problem domain
   - Comments that explain *why*, not just *what*
   - Breaking complex problems into manageable steps

**Common Student Challenges**:

1. **"Why not just use split()?"**
   - Great opportunity to discuss edge cases
   - Multiple consecutive spaces break naive split
   - Real-world data is messy

2. **"Which dimension should be which?"**
   - Array design requires understanding the problem
   - We process by *problem*, so problems should be first index
   - Visualize: `problems[whichProblem][whichNumber]`

3. **"Why can't we start everything at 0?"**
   - Perfect time to teach mathematical identity values
   - Test with examples: `0 * 5 = 0` (wrong!), `1 * 5 = 5` (correct!)
   - Connects CS to math curriculum

4. **"Can we use streams instead of loops?"**
   - Streams aren't in AP CSA subset
   - But this *is* a great example for post-AP enrichment!
   - Commented stream code shown in the solution

**Part 2 as an extension topic:**
- Discuss what makes it more complex (spatial parsing, position tracking)
- Show how the same problem can require different approaches
- Connect to real-world: How do different cultures/languages organize information?
- Science connection: Research mantis shrimp vision, cephalopod cognition

### Discussion Questions for Students

1. **Scanner vs. String methods**: When would you choose each approach? What are the tradeoffs?

2. **Data structure design**: How did choosing `problems[problemIndex][rowIndex]` instead of `problems[rowIndex][problemIndex]` make the code easier? What if we'd chosen differently?

3. **Identity values**: Find other programming situations where starting values matter. What happens if you get them wrong?

4. **Cross-cultural design**: How does understanding your user's perspective change how you design software? Can you think of real-world examples where this matters?

5. **Wookie efficiency**: If Chewbacca were to solve this problem, would he use the same algorithm? What about a protocol droid? How does the solver's nature influence the solution?

6. **Mantis shrimp mathematics**: If mantis shrimp can see 16 color receptors, might they have different number systems? How would that change the math?

### Extensions and Enrichment

1. **Vertical reading systems**: Research real writing systems that read vertically (Chinese, Japanese, Mongolian). How does this affect data processing?

2. **Mantis shrimp vision**: Look up actual research on mantis shrimp color perception. They don't see "more colors"—they process color information differently. What can this teach us about data processing?

3. **Alternative data formats**: Redesign the worksheet format. What would be easier to parse? What information would you lose?

4. **Performance analysis**: Time the Scanner approach vs. various String manipulation approaches. Which is faster? Does it matter for this problem size?

5. **Parser comparison**: Implement Part 1 using different parsing strategies (Scanner, split with regex, charAt loops). Compare readability and maintainability.

6. **Real Wookie math**: Design a vertical reading algorithm that might represent how Wookies process information. What would be different from the cephalopod approach?

### Conclusion

Sometimes the best lessons come from the most desperate situations. Trapped in a trash compactor with walls closing in, Luke and Chewie learned that:
- Clean, robust code (using Scanner) beats clever hacks
- Understanding your data structure (2D arrays) makes processing straightforward  
- Mathematical foundations (identity values) matter in programming
- Different perspectives (species/cultures) can see completely different things in the same data

And most importantly: when facing a multi-species math puzzle in a trash compactor, always bring a Wookie.

**May the Scanner be with you.**

---

### Appendix: Complete Part 1 Code with Annotations

```java
@Override
public String part1(ArrayList<String> input) {
    int rowCount = input.size() - 1;  // Last line is operators, not numbers
    
    // STEP 1: Count problems using Scanner (whitespace-tolerant)
    Scanner firstLineScanner = new Scanner(input.get(0));
    int problemCount = 0;
    while (firstLineScanner.hasNextLong()) {
        firstLineScanner.nextLong();  // Read and discard, just counting
        problemCount++;
    }
    firstLineScanner.close();

    // STEP 2: Build 2D array to organize data
    // problems[whichProblem][whichNumber]
    long[][] problems = new long[problemCount][rowCount];
    
    for (int row = 0; row < rowCount; row++) {
        Scanner lineScanner = new Scanner(input.get(row));
        int problemIndex = 0;
        
        // Scanner automatically skips whitespace!
        while (lineScanner.hasNextLong()) {
            problems[problemIndex][row] = lineScanner.nextLong();
            problemIndex++;
        }
        lineScanner.close();
    }
    
    // STEP 3: Read operators (also using Scanner)
    Scanner operatorScanner = new Scanner(input.get(rowCount));
    String[] operators = new String[problemCount];
    int operatorIndex = 0;
    
    while (operatorScanner.hasNext()) {
        operators[operatorIndex] = operatorScanner.next();
        operatorIndex++;
    }
    operatorScanner.close();

    // STEP 4: Process each problem
    long grandTotal = 0;
    
    for (int problemIndex = 0; problemIndex < problemCount; problemIndex++) {
        // Identity value: 1 for multiply, 0 for add
        long result = operators[problemIndex].equals(MULTIPLY) ? 1 : 0;

        // Apply operation to all numbers in this problem
        for (int numberIndex = 0; numberIndex < rowCount; numberIndex++) {
            if (operators[problemIndex].equals(MULTIPLY)) {
                result *= problems[problemIndex][numberIndex];
            } else {
                result += problems[problemIndex][numberIndex];
            }
        }
        
        grandTotal += result;
    }

    return String.valueOf(grandTotal);
}
```

**Key observations:**
- Three separate Scanners for three different purposes (count, data, operators)
- Each Scanner is closed after use (resource management)
- 2D array makes the nested loop structure natural
- Identity value prevents off-by-one errors in accumulation
- Descriptive variable names from the problem domain (problemIndex, not i)
