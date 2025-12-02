# Interview: Introduction to Advent of Code

*A conversation with Mr. Cerruti about participating in Advent of Code with AP CSA constraints*

**Reading Time:** 5-7 minutes for teachers | 4-5 minutes for students

**Note:** This interview format was created using AI to simulate student reporter questions, with Mr. Cerruti providing authentic responses about his Advent of Code participation and teaching philosophy. The conversational format is designed to make the information more accessible and engaging.

---

## The Interview

**Reporter (Alex):** Hi Mr. Cerruti! Thanks for agreeing to this interview. I've seen you working on something called "Advent of Code" during your planning period. What exactly is that?

**Mr. Cerruti:** Advent of Code is a really cool event. Each day in December for the length of the event a programming challenge is published. People from all over the world take part by attempting to solve the puzzle. There is a lot of community around sharing answers and helping others find answers. The puzzles begin easy and get harder each day.

**Reporter (Alex):** That sounds amazing! So it's like a daily programming puzzle challenge through December? I notice you said "for the length of the event" - does it go all the way to Christmas?

**Mr. Cerruti:** In past years it was 25 days (although I think day 25 wasn't a real challenge.) This year it's been shortened to 12 days. This is good for me because we are still in school all the way through.

**Reporter (Alex):** Oh, that's interesting! So normally it would run through Christmas Day, but this year it's just 12 days? That actually works out well with the school schedule. I'm curious - you mentioned there's a big community around this. Are people competitive about it, or is it more collaborative?

**Mr. Cerruti:** One of the features that I most enjoyed was a global leaderboard. People competed to be the first in the world to submit a correct solution. Unfortunately AI ruined that so the global leaderboard is gone. You can still have private leaderboards and those are better anyway. They allow you to compete against people at the same level and set your own rules including rules around AI. You know that I don't believe you can completely remove AI from code development but just like with class there are good and bad ways to use it depending on the task. Did I answer your question?

**Reporter (Alex):** Yes, definitely! Though I'm sad to hear AI spoiled the global competition - that must have been exciting to watch. But I like what you said about private leaderboards being better anyway. So there can be different rules for different groups?

*[pauses, then gets curious]*

Wait, you mentioned something interesting - you said there are "good and bad ways to use AI depending on the task." I know in our AP CSA class you let us use GitHub Copilot sometimes but not for certain assignments. How does that work with Advent of Code? Are you using AI for these challenges, or is that one of those times where it wouldn't be appropriate?

**Mr. Cerruti:** I solve it without AI in the spirit of the competition, I am also solving it using language available to AP CSA students (as much as I can.) I do have some level of typing help on, but try to avoid completion of blocks of code. Once I've completed the challenge I will go back and look at other ways to solve the puzzle, this may involve AI or visiting the subreddit. I also sometimes watch specific YouTube video people produce for AoC.

I will probably have to turn to AI towards the end of the competition to help me by giving hints.

**Reporter (Alex):** That makes a lot of sense - solve it yourself first to get the real learning experience, then use AI and community resources to see other approaches afterward. That's kind of like doing homework on your own first, then checking your work with the answer key.

You mentioned something about "language available to AP CSA students" - what does that mean? Are you limiting yourself to certain Java features?

**Mr. Cerruti:** The AP Computer Science A contains a subset of the Java language that students are expected to know for the test. They are not limited to this subset but they need to be able to use it to solve problems. This AP Subset from the Quick Reference is what I try to stick with.

**Reporter (Alex):** Oh wow, so you're challenging yourself to solve these problems using only what we're expected to know for the AP exam? That's... actually really cool! I mean, I've seen some of the solutions people post online with all these fancy Java features we haven't learned yet. It's kind of intimidating.

So if you're documenting all this, does that mean other AP CSA students or teachers could look at your solutions and actually understand them? Like, they wouldn't need to know advanced Java stuff?

**Mr. Cerruti:** Yes, I actually do writeups so other teachers can look at what learning objectives are covered to see how they can use them in their classes. Students could probably look too if they wanted extra work in a certain topic. However I need to be clear, the way I solve it may not be the only way or the best way.

**Reporter (Alex):** That's really humble of you to say! I think it's cool that you're showing there's not just one "right" way to solve problems. That's actually one thing I've noticed in your class - you're always asking us "what's another way to approach this?"

*[flips through notes]*

So let me make sure I understand the big picture here. You're participating in this worldwide programming challenge, but you're adding extra constraints - using only AP subset Java and no AI for the actual solving. Then you write up explanations for other teachers. That sounds like a lot of extra work! What made you decide to do all that instead of just solving the puzzles for fun?

**Mr. Cerruti:** The CSTA has standards for Computer Science teachers that span not just technical content but also building professional communities of support. I'm learning in a way that makes me better at teaching and I am sharing what I am learning with others to help them be better too. My hope is a little extra effort on my part spreads out and improves learning in some small way for a lot of students.

**Reporter (Alex):** Wow, I didn't know there were teaching standards specifically for CS teachers. That's really thoughtful - like, you're not just thinking about teaching us, but also helping other teachers teach their students better. That's... kind of meta, actually.

*[pauses, looking curious]*

Okay, so I have to ask - has this been hard? I mean, some of those later Advent of Code problems get pretty challenging, right? And you're doing it with one hand tied behind your back by limiting yourself to the AP subset. Have you run into any problems where you were like "man, I wish I could use [some advanced Java feature]"?

**Mr. Cerruti:** All the time! With the 2025 subset now including Scanner and Integer.parseInt a lot of pain points have gone away.

I can demonstrate how you can parse integers without it, but it would be a annoying to do every time.

Sometimes I have to use other features, especially later in the challenges. Structures like maps are really useful in a lot of the challenges. But I find that attempting to stay in bounds challenges me to think about the problem like one of my students.

**Reporter (Alex):** Wait, you can parse integers *without* `Integer.parseInt`? How does that even work? 

*[shakes head]*

Never mind, I don't think I want to know - that sounds painful! But I get what you're saying. By limiting yourself, you're kind of putting yourself in our shoes. That's actually really smart teaching - like, you remember what it's like to *not* have all the fancy tools.

You mentioned maps aren't in the AP subset. What do you do when you really need something like that? Do you just break the rules, or is there a workaround?

**Mr. Cerruti:** I will break the rules but I document it in my writeup. After I finish my solution and start looking around I can find a method that won't break the rules. I update the code and the writeup and try to give credit to the author.

**Reporter (Alex):** Oh, so it's not like "never ever break the rules" - it's more like "try your best, but be honest about when you had to." And then you go back and fix it if you find a better way. That's actually a really good model for learning. Like, sometimes you have to use what you know to solve the problem, even if it's not perfect, and then refine it later.

*[gets excited]*

Wait, so does that mean in your writeups you might show both versions? Like "here's how I solved it first with maps, and here's the AP-compliant version I figured out later"? That could actually be really helpful for understanding the trade-offs.

Okay, last question before I let you get back to today's puzzle - if a student wanted to try Advent of Code themselves, what advice would you give them? Should they wait until they're more advanced, or could someone in AP CSA right now give it a shot?

**Mr. Cerruti:** It's never too early to get started. If you can't do day 2 today, then you can always go and do day 1 from a past year.

If you think you are close, check the subreddit, but be careful of spoilers. You can even post your own code and get debugging help.

But I think today's students have a big advantage. Just tell your AI in your IDE what you are doing. Remind it not to give you answers but to ask you questions or provide clarifications on how things work or help you find patterns you aren't seeing. A tip, if you are using Copilot Chat in Visual Studio Code from Agent to Ask. 

AI can really help you learn, if you use it in the right way.

**Reporter (Alex):** That's really encouraging! I like that you're saying start where you are, not where you think you should be. And the AI tip is gold - switching from Agent to Ask mode so it guides instead of solving. I'm definitely going to remember that.

*[closes notebook]*

This has been really helpful, Mr. Cerruti. I think a lot of students don't realize that teachers are still learning and challenging themselves too. It's cool to see you doing something that's both fun for you and helps the CS teaching community. 

Thanks for taking the time to explain all this! I'll make sure to check out your writeups - and maybe I'll give Day 1 from a past year a try myself.

---

## Key Takeaways

### For Students
- **Start where you are**: It's never too early to try Advent of Code
- **Use AI wisely**: Switch Copilot to Ask mode for guidance, not solutions
- **Community support**: The subreddit can help when you're stuck (watch for spoilers!)
- **Progressive challenge**: Start with Day 1 from past years if current days are too hard

### For Teachers
- **Professional development**: AoC aligns with CSTA standards for building learning communities
- **Curriculum mapping**: Problems connect to specific AP CSA Learning Objectives
- **Constraint as pedagogy**: Limiting yourself to AP subset helps you think like students
- **Transparency models learning**: Document when you break rules and how you fix it
- **AI integration**: Show appropriate use cases (post-solution analysis vs. initial problem-solving)

### About This Project

This interview introduces the broader project goals:
- Document AP CSA-compliant solutions to Advent of Code 2025
- Map problems to specific Learning Objectives
- Provide teaching resources for CS educators
- Model continuous professional learning
- Share problem-solving strategies accessible to AP students

## Resources

- [Advent of Code Official Site](https://adventofcode.com)
- [Advent of Code Subreddit](https://reddit.com/r/adventofcode)
- [AP CSA Java Quick Reference](https://apcentral.collegeboard.org/media/pdf/ap-computer-science-a-java-quick-reference_0.pdf)
- [CSTA Teacher Standards](https://csteachers.org/teacherstandards)

---

*This interview was conducted on December 2, 2025, after completing Day 2 of Advent of Code 2025.*
