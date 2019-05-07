function batchTraining(Patterns, ExpectedOutputs)
	global hiddenLayers;
	global maxError;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;
	global currentError;

	batchForwardStep(Patterns);
	#calculate Deltas
	deltaCalculation(ExpectedOutputs, Outputs{hiddenLayers + 1});

	#update weights
	batchWeightUpdate(Patterns);

	Errors = (ExpectedOutputs - Outputs{hiddenLayers + 1}) .** 2;
	sumAll = Errors * (zeros(size(Patterns)(2), 1) + 1);
	currentError = sumAll / (2 * size(Patterns)(2))
	
endfunction