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

## [sentiment_classifier.py](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis/sentiment_classifier.py#L47-L71)

The solution is written in python, but is used in Java project. This is enabled by the py4j library. More info can be found in a write-up about [Bayesian network](/game-of-points/agent-reasoning#used-libraries).

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

### [Training code](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis/sentiment_classifier.py#L47-L71)

```python
def train_model(self, sentences, labels, epochs=15, batch_size=32):
    try:
        # Convert labels to one-hot encoding: [1, 0, 0], [0, 1, 0], [0, 0, 1]
        labels = tf_keras.utils.to_categorical(labels, num_classes=3)

        # Define early stopping function callback
        early_stopping = tf_keras.callbacks.EarlyStopping(
            monitor='val_loss', # monitor validation loss
            patience=3, # stop when validation loss doesn't get smaller for more than 3 epohs.
            restore_best_weights=True # when stopped, use not the latest epohs results, but the best possible results.
        )
        
        # Keras fit function gets called which calculates loss and gradiends, 
        # then applies the Adam optimizer to update model weights and does so for all 32 batches, for 15 epohs
        self.model.fit(
            np.array(sentences),
            np.array(labels),
            epochs=epochs,
            batch_size=batch_size,
            validation_split=0.2,
            callbacks=[early_stopping]
        )
        
        logger.info("Model trained successfully.")
        return "Training completed"
    except Exception as e:
        logger.error(f"Error during training: {e}")
        raise
```

## Usage