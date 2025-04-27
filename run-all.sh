#!/bin/bash
set -e

PY1_PID=
PY2_PID=
QUARKUS_PID=
NGROK_PID=

trap 'echo "🧹 Cleaning up..."; kill $PY1_PID $PY2_PID $QUARKUS_PID $NGROK_PID 2>/dev/null || true' EXIT

cd ./bayes-net
source .venv/bin/activate
python bayesian_network.py > ../logs/bayesian_network.log 2>&1 &
PY1_PID=$!

cd ../sentiment-classifier
source .venv/bin/activate
python sentiment_classifier.py predict > ../logs/sentiment_classifier.log 2>&1 &
PY2_PID=$!

echo "⏳ Launching bayes-net and sentiment classifier services..."
while ! nc -z localhost 25334; do sleep 0.5; done
while ! nc -z localhost 25336; do sleep 0.5; done
echo "✅ Bayes-net and sentiment classifier are ready!"

cd ../game-of-points-be
nohup ./mvnw quarkus:dev > ../logs/quarkus_main_server.log 2>&1 &
QUARKUS_PID=$!

echo "⏳ Launching game backend..."
while ! nc -z localhost 8080; do sleep 0.5; done
echo "✅ Game backend is ready."

# 🌐 Start ngrok
nohup ngrok http --domain=rizvan.ngrok.dev 8080 > ../logs/ngrok.log 2>&1 &
NGROK_PID=$!

sleep 1

cd ../game-of-points-fe
echo "⏳ Launching game frontend..."
npm run dev
while ! nc -z localhost 5173; do sleep 0.5; done
echo "✅ Game frontend is ready"