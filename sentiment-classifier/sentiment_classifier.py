import argparse
import logging
import os
os.environ['CUDA_VISIBLE_DEVICES'] = ''
import tensorflow as tf
import tensorflow_hub as hub
import tf_keras  # Import tf_keras to use it instead of tf.keras
import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from py4j.java_gateway import JavaGateway, GatewayParameters, CallbackServerParameters
from py4j.java_collections import ListConverter

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class SentimentAnalysisModel:
    def __init__(self):
        # Load the pre-trained model and build the classifier
        hub_url = "https://tfhub.dev/google/nnlm-en-dim50/2" # alternative to bert
        self.pretrained_embedding_layer = hub.KerasLayer(hub_url, input_shape=[], dtype=tf.string, trainable=True)

        self.model = tf_keras.Sequential([
            hub.KerasLayer(hub_url, input_shape=[], dtype=tf.string, trainable=True),
            tf_keras.layers.Dense(64, activation='relu', kernel_regularizer=tf_keras.regularizers.l2(0.01)),
            tf_keras.layers.Dropout(0.3),
            tf_keras.layers.Dense(32, activation='relu', kernel_regularizer=tf_keras.regularizers.l2(0.01)),
            tf_keras.layers.Dropout(0.3),
            tf_keras.layers.Dense(3, activation='softmax')  # three output nodes for three classes
        ])

        self.model.compile(optimizer=tf_keras.optimizers.Adam(learning_rate=0.001),
                           loss='categorical_crossentropy', metrics=['accuracy'])
        logger.info("SentimentAnalysisModel initialized.")

    def __str__(self):
        return "SentimentAnalysisModel instance"

    def __repr__(self):
        return self.__str__()

    def toString(self):
        return self.__str__()

    def train_model(self, sentences, labels, epochs=15, batch_size=32):
        try:
            # Convert labels to one-hot encoding: [1, 0, 0], [0, 1, 0], [0, 0, 1]
            labels = tf_keras.utils.to_categorical(labels, num_classes=3)

            # Define early stopping
            early_stopping = tf_keras.callbacks.EarlyStopping(
                monitor='val_loss', # rezultatu nuokrypis nuo realiu klasiu
                patience=3, # tesia mokyma max 3 epochas po to, kai loss nustoja mazeti
                restore_best_weights=True # po sustabdymo, vietoje naujausios epohos svoriu, naudoja geriausius rezultatus sukurusius svorius
            )

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

    def save_model(self, model_path="sentiment_model.keras"):
        try:
            # Save the trained model
            self.model.save(model_path)
            logger.info(f"Model saved to {model_path}.")
            return f"Model saved to {model_path}"
        except Exception as e:
            logger.error(f"Error saving model: {e}")
            raise

    def load_model(self, model_path="sentiment_model.keras"):
        try:
            # Load the model
            self.model = tf_keras.models.load_model(model_path, custom_objects={'KerasLayer': hub.KerasLayer})
            logger.info(f"Model loaded from {model_path}.")
            return f"Model loaded from {model_path}"
        except Exception as e:
            logger.error(f"Error loading model: {e}")
            raise

    def evaluate_model(self, sentences, labels):
        try:
            # Convert labels to one-hot encoding
            labels = tf_keras.utils.to_categorical(labels, num_classes=3)

            # Evaluate the model
            loss, accuracy = self.model.evaluate(np.array(sentences), np.array(labels))
            logger.info(f"Model evaluation completed. Loss: {loss}, Accuracy: {accuracy}")
            return {"loss": loss, "accuracy": accuracy}
        except Exception as e:
            logger.error(f"Error during evaluation: {e}")
            raise

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

    def convert_to_java_list(self, python_list):
        try:
            # Convert python list to java list
            java_list = ListConverter().convert(python_list, self.gateway._gateway_client)
            return java_list
        except Exception as e:
            logger.error(f"Error during conversion to Java list: {e}")
            raise

    def get_prediction(self, sentence):
        predictions = self.predict_sentiment([sentence])
        return predictions[0]

    def set_gateway(self, gateway):
        self.gateway = gateway

def train_mode(csv_file, epochs=15, batch_size=32):
    # Read CSV file
    logger.info(f"Reading training data from {csv_file}")
    data = pd.read_csv(csv_file)

    # Map sentiment labels to numerical values
    sentiment_mapping = {
        "Pessimistic": 0,
        "Neutral": 1,
        "Optimistic": 2
    }

    # Map sentiment labels to corresponding numerical values
    data['label'] = data['Sentiment'].map(sentiment_mapping)

    # Extract sentences and labels
    sentences = data['Sentence'].values
    labels = data['label'].values

    # Split data into training and test sets
    X_train, X_test, y_train, y_test = train_test_split(sentences, labels, test_size=0.2, random_state=42)

    # Initialize model
    model = SentimentAnalysisModel()

    # Train the model
    model.train_model(X_train, y_train, epochs, batch_size)

    # Evaluate the model
    evaluation_result = model.evaluate_model(X_test, y_test)
    logger.info(f"Evaluation result: {evaluation_result}")

    # Save the model
    model.save_model("sentiment_model.keras")

def prediction_mode():
    # Initialize model and load pre-trained weights
    model = SentimentAnalysisModel()
    model.load_model("sentiment_model.keras")

    java_host_name = os.getenv("JAVA_GATEWAY_HOST")
    python_host_name = os.getenv("PYTHON_GATEWAY_HOST")

    gateway_host_name = "localhost" if java_host_name is None else java_host_name
    callback_host_name = "localhost" if python_host_name is None else python_host_name

    # Setup Py4J gateway
    gateway = JavaGateway(
        gateway_parameters=GatewayParameters(address=gateway_host_name, port=25335, auto_convert=True),
        callback_server_parameters=CallbackServerParameters(address=callback_host_name, port=25336),
        python_server_entry_point=model
    )
    model.set_gateway(gateway)

    logger.info("Python server for sentiment analysis is ready.")
    # The server will keep running, waiting for requests from Java

if __name__ == "__main__":
    # Initialize arguments parser
    parser = argparse.ArgumentParser(description='Sentiment Analysis Model')

    # Add arguments to arg parser
    parser.add_argument('mode', nargs='?', default='predict', help="Mode: 'train' for training, 'predict' for prediction, 'test' for testing a single sentence")
    parser.add_argument('--csv', type=str, help="Path to the CSV file for training", default='')
    parser.add_argument('sentence', nargs='?', help="Sentence to test in test mode", default='')

    # Parse command line arguments into python object
    args = parser.parse_args()

    # Choose future action pipeline based on user selected mode
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
