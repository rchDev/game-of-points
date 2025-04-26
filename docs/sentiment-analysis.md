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

The solution is written in python, but is used in Java project. 
This is enabled by the [py4j](https://www.py4j.org/) library. 
More info can be found in a write-up about [Agent's reasoning process](/game-of-points/agent-reasoning), 
in a Bayesian networks "Used libraries" section.

The script can be launched in three modes: training, testing or prediction mode.

```python
    if args.mode == 'train':
        if args.csv:
            train_mode(args.csv)
        else:
            logger.error("CSV file path must be provided for training mode.")
    elif args.mode == 'test':
        if args.sentence:
            print(f"sentence: {args.sentence}")
            model = SentimentAnalysisModel()
            model.load_model("sentiment_model.keras")
            prediction = model.predict_sentiment([args.sentence])
            print(f"prediction: {prediction}")
            print(f"The sentiment is: {prediction[0]}")
        else:
            logger.error("Sentence must be provided for test mode.")
    else:
        prediction_mode()
```

## Training mode

### Command

{: .note}
Only csv parsing was implemented...

**In project's root run:**

```shell
cd game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis
poetry run python sentiment_classifier.py train --csv <data_file_name.csv>
```

### **Data**
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

## Testing mode:

### Command

**In project's root run:**

```shell
cd game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis
poetry run python sentiment_classifier.py test "your sentence, describing the mood"
```

### What's going on

This mode can be used to test or demonstrate the classification of one sentence. It doesn't spin up the py4j server.
All it does, is:
1. Construct the model.
2. Load pretrained weights.
3. Pass the sentence that was provided in command line arguments.
4. Send the sentence and the predicted class to the standard output.

```python
...
elif args.mode == 'test':
    if args.sentence:
        print(f"sentence: {args.sentence}")
        model = SentimentAnalysisModel()
        model.load_model("sentiment_model.keras")
        prediction = model.predict_sentiment([args.sentence])
        print(f"prediction: {prediction}")
        print(f"The sentiment is: {prediction[0]}")
...
```

## Prediction mode:

### Command

**In project's root run:**

```shell
cd game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis
poetry run python sentiment_classifier.py predict
```

### What's going on

This mode is for running sentiment (mood) prediction py4j server, that can be used by Java game server.
In this mode, fa unction called: [prediction_mode()](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis/sentiment_classifier.py#L172-L186) is called, where:
1. SentimentAnalysis model is initialized.
2. Pretrained weights are loaded from a file.
3. py4j gateway server is started.

```python
...
def prediction_mode():
    # Initialize model and load pre-trained weights
    model = SentimentAnalysisModel()
    model.load_model("./sentiment_model.keras")

    # Setup Py4J gateway
    gateway = JavaGateway(
        gateway_parameters=GatewayParameters(port=25335, auto_convert=True),
        callback_server_parameters=CallbackServerParameters(port=25336),
        python_server_entry_point=model
    )
    model.set_gateway(gateway)

    logger.info("Python server for sentiment analysis is ready.")
    # The server will keep running, waiting for requests from Java
...
```

Then, before the creation of an agent, on the Java side, inside GameResource controller ([see this](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/resources/GameResource.java#L60-L66)).
In the case that player has provided his mood description during the questioning, we:
1. call a method: get_prediction() and pass it a mood description.
2. [get_prediction()](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis/sentiment_classifier.py#L130-L132) calls a method: [self.predict_sentiment()](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis/sentiment_classifier.py#L106-L119), which takes a sentence, and returns a list of classes for each sentence (which is always one) (I know it's weird, don't judge...)
3. On Java side we receive the class as a string, convert it into an enum.
4. Then store this mood enum in player answers store.
5. Next, store the answer in player answers database, which is then used for creating a Bayesnet.

```java
...
if (playerAnswers.isPresent() && playerAnswers.get().getMoodDescription().isPresent()) {
    var playerMoodDescription = playerAnswers.get().getMoodDescription().get();
    var moodClass = pythonGateway.getSentimentAnalyser().get_prediction(playerMoodDescription);
    var mood = PlayerMood.fromName(moodClass);
    playerAnswers.get().setMood(mood);
    weaponService.addWeaponWithMood(playerWeapon, mood);
}
...
```

Inside [predict_sentiment()](https://github.com/rchDev/game-of-points/blob/main/game-of-points-be/src/main/java/io/rizvan/beans/actors/agent/sentiment-analysis/sentiment_classifier.py#L106-L119)
we:
1. convert a single string into a numpy array for compatibility with keras stuff...
2. we use the trained model to give us a prediction array for each sentence. It's an array containing an array that looks something like this: [pessimistic, neutral optimistic] -> [0.2, 0.68, 0.12]
3. we convert this array of arrays into an array of class names by using an index returned by np.argmax and indexing into classes array.
4. Then we return a list of class names. The list will always contain only 1 element, because there is no way to pass more than one sentence in an argument list.

```python
...
def predict_sentiment(self, sentences):
    try:
        # Convert input sentences to numpy array
        sentences_array = np.array(sentences)
        # Predict
        predictions = self.model.predict(sentences_array)
        # Convert predictions to class labels
        classes = ["pessimistic", "neutral", "optimistic"]
        predicted_classes = [classes[np.argmax(pred)] for pred in predictions]
        logger.info(f"Predicted classes: {predicted_classes}")
        return predicted_classes
    except Exception as e:
        logger.error(f"Error during prediction: {e}")
        raise
...
```