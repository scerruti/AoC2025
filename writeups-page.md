---
layout: page
title: "Writeups"
permalink: /writeups/
---

# Daily Writeups

Browse all daily problem writeups, organized chronologically. Each writeup includes the problem approach, AP CSA compliance notes, and learning objectives addressed.

<div class="post-list">
{% assign writeup_posts = site.posts | where: "categories", "writeup" | sort: "date" %}
{% for post in writeup_posts %}
  {% unless post.draft == true %}
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
  {% endunless %}
{% endfor %}
</div>
