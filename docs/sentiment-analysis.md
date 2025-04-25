---
layout: default
title: Sentiment Analysis
nav_order: 3
parent: Deep Lore
permalink: /sentiment-analysis/
---

# Sentiment Analysis

The point of sentiment analysis in my application is to take a mood description and assign it one of three classes:
1. Optimistic
2. Neutral
3. Pessimistic

## Training

### Command

**In projects root:**

{: .note}
Only csv parsing was implemented...

```shell
cd game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis
poetry run python sentiment_classifier.py train --csv <data_file_name.csv>
```

### Data
There are many pretrained emotion/sentiment classifiers, but since I had to build my own. 
The first and the hardest problem I had to tacle was: **getting labeled data**.

At first, I naively tried to use popular LLMs on the internet to generate unique player answers and their class labels, 
to a proposed question: "how are feeling before this upcoming match?"
As I recall, I've tried ChatGPT 4o, some version of Claude and Gemini.

At the time of creating this model, at around Aug. 2024, none of these LLMs could generate unique data. 
For example in a list of 100 answers only 15-30 answers would be unique, the rest would be duplicates with different labels.
And no matter how elaborate my prompting attempts were, I couldn't get even 100 unique answers with correct labels.

I still tried to use that data in training, but as my intuition was telling me, the data was completely **useless:**
1. **unbalanced** - too many labels of one class (e.g. optimistic),
2. **lots of duplicates** - same exact answer showing up many times,
3. **not diverse** - comes from a single source (my mind, some probably from LLM),
4. **no noise** - sentences were perfect: no typos, no grammatical errors, no slang terms, correct punctuation,
5. **inconsistent labels** - duplicated values having different labels.

**The result of using this data:** incredible training and test accuracy >0.98, but terrible real world results.
The trained model couldn't even classify the simplest answers, like: "I'm feeling great."

### Training metadata



## Usage