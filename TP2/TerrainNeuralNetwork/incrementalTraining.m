function incrementalTraining(Patterns, ExpectedOutputs)
	global UnitsQuantity;
	global hiddenLayers;
	global maxError;
	global Outputs;
	global currentError;
	global Errors;
	global silent;
	global showPlot;
	# Adaptive eta
	global adaptiveEta;
	global Weights;
	global OldWeights;
	global epoch;
	global adaptiveEtaDeltaSteps;

	inputSize		 = size(Patterns)(2);
	inputOrder 		 = shuffle(1 : inputSize, inputSize);
	acumError  		 = 0;
	accumEpsilon	 = 0;

	if (adaptiveEta && epoch == 1)
		OldWeights = Weights;
	endif

	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		CurrentPattern  = CurrentPattern / norm(CurrentPattern); #normalize input
		Input 			= CurrentPattern;

		incrementalForwardStep(Input);

		ExpectedOutput = ExpectedOutputs(inputOrder(index));
		CurrOutput 	   = Outputs{hiddenLayers + 1};
		acumError += (ExpectedOutput - CurrOutput) ** 2;

		if(ExpectedOutput != CurrOutput)
			#calculate Deltas
			deltaCalculation(ExpectedOutput, CurrOutput);

			#update weights
			incrementalWeightUpdate(CurrentPattern);
		endif
	endfor
	
	# Update errors
	currentError = acumError / (2*inputSize);
	Errors = [Errors currentError];
	if (!silent)
		currentError
	endif
	if (showPlot)
		figure(1)
		plot(Errors);
	endif
	acumError = 0;

	if (adaptiveEta)
		adaptiveEtaFn()
	endif
endfunction