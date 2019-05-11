function batchTraining(Patterns, ExpectedOutputs)
	global hiddenLayers;
	global maxError;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;
	global currentError;

	Patterns = normalizePatterns(Patterns);
	batchForwardStep(Patterns);

	#calculate Deltas
	deltaCalculation(ExpectedOutputs, Outputs{hiddenLayers + 1});
	
	#update weights
	batchWeightUpdate(Patterns);

	#Errors = (ExpectedOutputs - Outputs{hiddenLayers + 1})# .** 2;
	#Outputs{hiddenLayers + 1}
	#sumAll = Errors * (zeros(size(Patterns)(2), 1) + 1);
	#currentError = sumAll / (2 * size(Patterns)(2))
	
endfunction

function [colNorms] = normalizePatterns(Patterns)
	colNorms = zeros(size(Patterns)(1), size(Patterns)(2));

	for col = 1 : size(Patterns)(2)
		currInput = Patterns(: , col);
		colNorms(:, col) = currInput / norm(currInput);
	endfor
endfunction