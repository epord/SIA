#!/bin/bash

trap '
  trap - INT
  kill -s INT "$$"
' INT

cp architecture.conf architecture.conf.bak
rm configurationResults.txt > /dev/null 2>&1
for trainingPercentage in $(seq 0.81 0.01 0.91)
  do
		echo "Testing with training percentage: $trainingPercentage"
		sleep 1
		sed -i "" "/.*trainingPercentage.*/s//global trainingPercentage = $trainingPercentage;/g" architecture.conf
		octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt
 		#cat architecture.conf
 done
cp architecture.conf.bak architecture.conf
rm architecture.conf.bak