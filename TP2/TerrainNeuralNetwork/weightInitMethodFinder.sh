#!/bin/bash

trap '
  trap - INT
  kill -s INT "$$"
' INT

cp architecture.conf architecture.conf.bak
rm configurationResults.txt > /dev/null 2>&1
for quantity in $(seq 1 1 5)
  do
		echo "Testing with $alphaMomentum nodes in the hidden layer"
		sleep 1
		currentSeed=$RANDOM
		sed -i "" "/.*rand(\"seed\",.*/s//rand(\"seed\", $currentSeed);/g" architecture.conf
		
		#random weights
		sed -i "" "/.*weightInitMethod.*/s//weightInitMethod = 0;/g" architecture.conf
		octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt
		#cat architecture.conf
		
		#fan-in
		sed -i "" "/.*weightInitMethod.*/s//weightInitMethod = 1;/g" architecture.conf
		octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt
 		#cat architecture.conf
 done
cp architecture.conf.bak architecture.conf
rm architecture.conf.bak