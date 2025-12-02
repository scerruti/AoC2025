---
layout: page
title: "Case Studies"
permalink: /case-studies/
---

# Case Studies

In-depth explorations of algorithm design, optimization techniques, and teaching strategies. These case studies document the journey from naive solutions to optimized implementations, with pedagogical insights aligned with CSTA Teacher Standards.

<div class="post-list">
{% assign case_study_posts = site.posts | where: "categories", "case-study" | sort: "date" %}
{% for post in case_study_posts %}
  <article class="post-item">
    <h2>
      <a href="{{ post.url | relative_url }}">{{ post.title }}</a>
    </h2>
    <p class="post-meta">
      <time datetime="{{ post.date | date_to_xmlschema }}">{{ post.date | date: "%B %d, %Y" }}</time>
      {% if post.tags.size > 0 %}
        <br>
        <span class="tags">
        {% for tag in post.tags %}
          <span class="tag">{{ tag }}</span>
        {% endfor %}
        </span>
      {% endif %}
    </p>
  </article>
{% endfor %}
</div>

## What Are Case Studies?

These deep-dive analyses explore:

- **Algorithm Evolution**: From brute-force to optimized solutions
- **Refactoring Strategies**: How to improve code structure and performance
- **Teaching Approaches**: Classroom strategies and inquiry-based prompts
- **Professional Growth**: How teachers can model continuous learning
- **Standards Alignment**: Mapping to CSTA Teacher Standards and AP CSA Learning Objectives

Each case study documents the iterative development process, including false starts and corrections, to provide an authentic view of problem-solving.
