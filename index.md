---
layout: home
title: "Advent of Code 2025 - AP CSA Edition"
---

# Welcome to Advent of Code 2025 - AP CSA Edition

This site documents my journey through [Advent of Code 2025](https://adventofcode.com/2025), with all solutions written to comply with the **AP Computer Science A (AP CSA) Java subset**.

## About This Project

Advent of Code provides excellent algorithmic challenges, but many solutions use advanced Java features beyond the AP CSA curriculum. This project demonstrates that interesting problems can be solved using only the AP subset, making these solutions valuable for:

- **AP CSA Students**: See real-world applications of course concepts
- **AP CSA Teachers**: Use as teaching examples and case studies
- **Self-Learners**: Understand fundamental programming without advanced features

## What You'll Find Here

### 📝 Daily Writeups
Each day includes a detailed writeup covering:
- Problem summary and approach
- AP CSA Learning Objectives addressed
- Teaching notes and pedagogical insights
- Code explanations with compliance notes

### 📚 Case Studies
In-depth explorations of:
- Algorithm optimization techniques
- Refactoring from naive to efficient solutions
- Teaching strategies and classroom applications
- Alignment with CSTA Teacher Standards

### ✅ AP CSA Compliance
All solutions use only:
- Primitive types (`int`, `double`, `boolean`)
- `String`, `ArrayList`, and 1D arrays
- Standard control structures (`if`, `for`, `while`)
- Methods from the [Java Quick Reference](https://apcentral.collegeboard.org/media/pdf/ap-computer-science-a-java-quick-reference_0.pdf)

**Note**: Occasionally, problems require exceptions (like `long` for large numbers). These are clearly documented with explanations.

## Browse Content

### By Category
- [All Writeups]({{ site.baseurl }}{% link writeups.md %}) - Daily problem solutions
- [Case Studies]({{ site.baseurl }}{% link case-studies.md %}) - Deep dives into algorithms and teaching

### By Day
{% for post in site.posts %}
  {% if post.categories contains 'writeup' %}
- [{{ post.title }}]({{ post.url | relative_url }}) - {{ post.date | date: "%B %d, %Y" }}
  {% endif %}
{% endfor %}

## Learning Objectives Coverage

This project addresses multiple AP CSA Learning Objectives (2025 CED), including:

- **LO 1.3.C**: Arithmetic expressions and operators
- **LO 1.15.B**: String methods and manipulation
- **LO 2.3.A**: Selection statements and branching logic
- **LO 2.7.B**: Iteration with while loops
- **LO 2.8.A**: Iteration with for loops
- **LO 4.3.A**: Working with 1D arrays
- **LO 4.9.A**: Traversing ArrayList collections
- **MOD-1**: Designing and implementing methods

## Resources

- [GitHub Repository](https://github.com/scerruti/AoC2025) - View the source code
- [Advent of Code](https://adventofcode.com) - Official AoC website
- [AP CSA Course Description](https://apcentral.collegeboard.org/courses/ap-computer-science-a) - Official College Board resource

## Notes

- **Important**: Solutions must be your own work. Per AoC rules, AI assistance is not allowed for solving puzzles. This project documents solutions after they've been completed.
- No puzzle text or input files from the AoC website are included in this repository, in accordance with AoC guidelines.
- This is an educational resource demonstrating AP subset compliance, not a solution key.

---

Happy coding and learning! 🎄✨
