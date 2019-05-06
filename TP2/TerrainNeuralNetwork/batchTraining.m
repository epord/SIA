function batchTraining(Patterns, ExpectedOutputs)
	global hiddenLayers;
	global maxError;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;


	batchForwardStep(Patterns);

	#calculate Deltas
	deltaCalculation(ExpectedOutputs, Outputs{hiddenLayers + 1});

	#update weights
	batchWeightUpdate(Patterns);

	currentError = acumError / 2
	printf("method not implemented\n");
endfunction