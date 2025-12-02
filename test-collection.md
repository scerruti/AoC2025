---
layout: home
title: Test Collection
---

# Test Collection Iteration

## All writeups (no filter):
{% for post in site.writeups %}
- {{ post.title }} - {{ post.path }}
{% endfor %}

## Filtered by category:
{% assign writeup_posts = site.writeups | where_exp: "item", "item.categories contains 'writeup'" %}
{% for post in writeup_posts %}
- {{ post.title }}
{% endfor %}

## Count: {{ site.writeups | size }}
