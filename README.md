# Advent of Code 2025

This repository contains my solutions and write-ups for the 2025 Advent of Code event.

**Note:** Solutions are written with the AP Computer Science A (AP CSA) Java subset in mind, for educational and reference purposes.

## 📚 [View the Full Documentation Site](https://scerruti.github.io/AoC2025/)

For complete writeups, case studies, and pedagogical notes, visit the [GitHub Pages site](https://scerruti.github.io/AoC2025/), which includes:
- Daily problem writeups with AP CSA Learning Objective mappings
- In-depth case studies on algorithm optimization and teaching strategies
- Alignment with CSTA Teacher Standards
- Browse by day, category, or learning objective tags

## Key AP CSA Learning Features (2025)

This project highlights AP Computer Science A (AP CSA) concepts and learning objectives through Advent of Code problems. Each day is annotated with relevant Java subset features and AP CSA Learning Objectives (LOs).

### [Day 1: Secret Entrance](https://scerruti.github.io/AoC2025/writeups/Day01/)
- **String parsing**: Using `substring` and `equals` to extract and compare characters.
- **Integer parsing**: Using `Integer.parseInt` to convert string input to numbers.
- **Loops**: Iterating over a `List` of input lines with an **enhanced `for` loop**.
- **Arithmetic and modulo**: Handling wraparound logic with addition, subtraction, and modulo operations.
- **Conditionals**: Using `if`, `else if`, and `else` to handle multiple cases.
- **AP CSA LOs:**
    - **LO 1.3.C: Arithmetic Expressions** (`%`, `/`)
    - **LO 1.15.B: String Methods** (`substring`, `equals`)
    - **LO 2.3.A / 2.5.A: Selection** (if/else, compound logic)
    - **LO 2.7.B: Iteration** (while loops)
    - **LO 4.7.A: Wrapper Classes** (`Integer.parseInt`)
    - **LO 4.8.A / 4.9.A: Data Collections** (ArrayList creation and enhanced for loop traversal)

### [Day 2: Gift Shop](https://scerruti.github.io/AoC2025/writeups/Day02/)
- **String Manipulation**: Extensive use of `substring` to detect repeating patterns.
- **Arrays**: Using `split` to parse comma-separated input into arrays.
- **Nested Loops**: Using nested iteration to test variable pattern lengths.
- **Data Types**: Note on using `long` (exception to subset) for large inputs.
- **AP CSA LOs:**
    - **LO 1.3.C: Arithmetic Expressions** (`%`, `/`, `+=`)
    - **LO 1.15.B: String Methods** (`substring`, `length`, `equals`)
    - **LO 2.3.A: Selection** (`if` statements)
    - **LO 2.7.B / 2.8.A: Iteration** (nested `for` loops)
    - **LO 4.3.A / 4.4.A: Arrays** (1D array creation and traversal)

### [Day 3: Lobby](https://scerruti.github.io/AoC2025/writeups/Day03/)
- **Greedy Algorithms**: Selecting optimal subsequences from sequences while maintaining order.
- **Array Manipulation**: Using arrays to store and dynamically adjust selected elements.
- **String to Integer Conversion**: Parsing individual digits with `Integer.parseInt` and `substring`.
- **Data Types**: Using `long` (exception to subset) for 12-digit numbers in Part 2.
- **AP CSA LOs:**

### [Day 4: Printing Department](https://scerruti.github.io/AoC2025/writeups/Day04/)
- **2D Arrays**: Using padded 2D arrays to eliminate boundary checking.
- **Cellular Automaton**: Simulating state changes based on neighbor counts.
- **Two-Phase Updates**: Mark-then-remove pattern to avoid state corruption.
- **Nested Loops**: Iterating through 2D grid structures.
- **AP CSA LOs:**
    - **LO 1.3.C: Arithmetic Expressions** (neighbor counting)
    - **LO 2.3.A: Selection** (conditional neighbor checks)
    - **LO 2.7.B / 2.8.A: Iteration** (nested loops for 2D traversal)
    - **LO 4.1.A / 4.2.A: 2D Arrays** (creation, traversal, modification)
    - **LO 4.9.A: Data Collections** (ArrayList for input parsing)

### [Day 5: Cafeteria](https://scerruti.github.io/AoC2025/writeups/Day05/)
- **State Machine Pattern**: Using boolean flag (`rangesDone`) to track parsing phases.
- **Object-Oriented Design**: Range class with encapsulation and helper methods.
- **Interval Merging**: Incremental range merging algorithm for Part 2.
- **Code.org Framework**: Structured problem-solving (Understand → Plan → Try → Reflect).
- **AP CSA LOs:**
    - **LO 1.3.C: Arithmetic Expressions** (range size calculations)
    - **LO 2.3.A: Selection** (if/else for case analysis)
    - **LO 2.7.B / 2.8.A: Iteration** (enhanced for loops, early exit with break)
    - **LO 4.1.A: Object-Oriented Design** (Range class, inheritance from Day)
    - **LO 4.8.A / 4.9.A: Data Collections** (ArrayList creation and traversal)

### [Day 6: Trash Compactor](https://scerruti.github.io/AoC2025/writeups/day06/)
- **Scanner Class**: Whitespace-tolerant parsing without regex (AP CSA compliant).
- **2D Arrays**: Organizing data by problem and number position.
- **Identity Values**: Mathematical foundations for multiplication (1) and addition (0).
- **Cross-Species Communication**: Same data, different parsing strategies (educational metaphor).
- **AP CSA LOs:**
    - **MOD-1.C**: Call methods on objects (Scanner methods)
    - **MOD-1.E**: Evaluate expressions using relational operators
    - **CON-2.A.1**: Use conditional statements (if/else, ternary)
    - **CON-2.B**: Represent iterative processes (nested loops)
    - **VAR-2.E.1 / VAR-2.E.2**: Create and traverse 2D arrays
    - **VAR-2.F**: Work with wrapper classes (Integer.parseInt)
- **CSTA Standards for CS Teachers:**
    - **2A**: Plan learning experiences advancing programming knowledge
    - **2B**: Use effective instructional strategies (Scanner comparison)
    - **2C**: Develop students' testing and refinement abilities
    - **3A**: Design assessments measuring student learning
    - **5B**: Model inclusive practices (diverse perspectives metaphor)

### [Day 7: Tachyon Beam Splitters](https://scerruti.github.io/AoC2025/writeups/day07/) (Beyond AP CSA)
- **⚠️ Beyond AP CSA 2025**: Recursion removed from curriculum in May 2026 exam
- **Recursion Fundamentals**: Base cases, recursive cases, call stack mechanics
- **Dynamic Programming**: Three approaches compared (iterative, memoization, bottom-up DP)
- **Performance Analysis**: Bottom-up DP 10× faster than stack-based approaches
- **HashMap Usage**: Memoization caching with String and Point keys
- **HashSet**: Tracking visited positions to prevent cycles
- **Educational Value**: Recursion remains essential for college CS courses
- **Topics Covered:**
    - **CON-2.B**: Iteration with explicit stack vs recursion
    - **VAR-2.E**: 2D arrays for field representation and DP table
    - **MOD-3.B**: HashSet and HashMap for tracking and caching
    - Factorial example demonstrating recursion fundamentals
    - Path counting with exponential branching (splitters)
    - Academic citation: Garcia & Craig (2025) on recursion in CS1 curricula


### [Day 8: Playground](https://scerruti.github.io/AoC2025/writeups/Day08/)
- **⚠️ Not Suitable for AP CSA**
- **Advanced Algorithms**: Requires minimum spanning tree/union-find, 3D geometry, and performance optimization (octree/ball tree).
- **Beyond Curriculum**: These topics are not covered in AP CSA and are not appropriate for introductory Java students.
- **Pedagogical Note**: This puzzle is best skipped for AP CSA. See the "not the droids you are looking for" post for details.

### [Day 9: Polygon Area Maximization](https://scerruti.github.io/AoC2025/writeups/Day09/)
- **Run-Length Encoding (RLE)**: Memory-efficient grid representation using ArrayList of arrays for row sections.
- **Polygon Filling**: Even-odd rule implementation for interior filling after boundary drawing.
- **Optimization Techniques**: Sorting pairs by area descending to find maximum early; row-wise filled length checks instead of per-cell verification.
- **Data Structures**: Custom FloorModel class with RLE for scalable grid operations.
- **Performance**: Handles large inputs (496 points, 122k pairs) in under 1 second through algorithmic optimizations.
- **AP CSA LOs:**
    - **LO 2.7.B: Iteration** (nested loops for pair checking)
    - **LO 4.3.A / 4.4.A: Arrays** (2D array concepts via RLE sections)
    - **LO 4.8.A / 4.9.A: Data Collections** (ArrayList for dynamic RLE storage)
    - **LO 5.3.A: Algorithm Analysis** (optimizing O(n²) pair checks with sorting and early termination)

## About

- See [Advent of Code](https://adventofcode.com) for event details and rules.
- This project is for reference and educational purposes.
- No puzzle text or input files from the AoC website are included, in accordance with event rules.

---
Feedback and suggestions are welcome!

## Tips for Advent of Code

- Test your solution with the provided examples before submitting.
- If stuck, re-read the puzzle and try building your own test cases.
- Do not include puzzle text or input files from the AoC website in your repo.
- Solutions must be your own work—AI assistance is not allowed for solving puzzles.

---
Happy coding and learning!