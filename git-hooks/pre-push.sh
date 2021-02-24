#!/bin/bash

echo "Running unit test 🥶🥶🥶"

./gradlew testDevDebug

status=$?

if [ "$status" = 0 ]; then
  echo "💃💃💃 All unit test pass successfully. Processing with pushing the code"
  exit 0
else
  echo 1>&2 "😤😤😤 Some test fails, Please check before pushing the code"
  exit 1
fi
