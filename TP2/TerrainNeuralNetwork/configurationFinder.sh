#!/bin/bash

trap '
  trap - INT
  kill -s INT "$$"
' INT

cp architecture.conf architecture.conf.bak
rm configurationResults.txt > /dev/null 2>&1
for nodesInLayer in $(seq 2 2 20)
  do
		echo "Testing with $nodesInLayer nodes in the hidden layer"
		sleep 1
		sed -i "" "/.*UnitsQuantity.*/s//global UnitsQuantity = [2, $nodesInLayer, 1];/g" architecture.conf
		octave multiLayerPerceptron.m --silent --auto-exit | tee -a configurationResults.txt
 done
cp architecture.conf.bak architecture.conf
rm architecture.conf.bak