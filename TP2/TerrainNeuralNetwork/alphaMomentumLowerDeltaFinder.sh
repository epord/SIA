#!/bin/bash

trap '
  trap - INT
  kill -s INT "$$"
' INT

cp architecture.conf architecture.conf.bak
rm configurationResults.txt > /dev/null 2>&1
for alphaMomentum in $(seq 0.2 0.01 0.3)
  do
		echo "Testing with $alphaMomentum nodes in the hidden layer"
		sleep 1
		sed -i "" "/.*alphaMomentum.*/s//global alphaMomentum = $alphaMomentum;/g" architecture.conf
		#octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt
 		cat architecture.conf
 done
cp architecture.conf.bak architecture.conf
rm architecture.conf.bak