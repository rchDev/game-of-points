#!/bin/bash
set -e

PY1_PID=
PY2_PID=
QUARKUS_PID=
NGROK_PID=

trap 'echo "🧹 Cleaning up..."; kill $PY1_PID $PY2_PID $QUARKUS_PID $NGROK_PID 2>/dev/null || true' EXIT

cd game-of-points-be/src/main/java/io/rizvan/beans/actors/agent
source ./venv-3.12/bin/activate
python ./baysian_network.py > baysian_network.log 2>&1 &
PY1_PID=$!

cd ./sentiment-analysis
source ./venv-3.12/bin/activate
python ./sentiment_classifier.py > sentiment_classifier.log 2>&1 &
PY2_PID=$!

echo "⏳ Launching bayes-net and sentiment classifier services..."
while ! nc -z localhost 25334; do sleep 0.5; done
while ! nc -z localhost 25336; do sleep 0.5; done
echo "✅ Services are ready!"

cd ../../../../../../../../../
./mvnw clean install
nohup ./mvnw quarkus:dev > quarkus_main_server.log 2>&1 &
QUARKUS_PID=$!

echo "⏳ Launching Quarkus..."
while ! nc -z localhost 8080; do sleep 0.5; done
echo "✅ Quarkus is ready."

# 🌐 Start ngrok
nohup ngrok http 8080 > ngrok.log 2>&1 &
NGROK_PID=$!

sleep 1

cd ../game-of-points-fe
echo "⏳ Launching Front end..."
npm run dev
while ! nc -z localhost 5173; do sleep 0.5; done
echo "✅ Front end is ready"