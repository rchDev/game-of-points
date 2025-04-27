FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 25336

ENTRYPOINT ["python", "sentiment_classifier.py"]
CMD []
