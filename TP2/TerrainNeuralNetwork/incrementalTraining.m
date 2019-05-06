function incrementalTraining(Patterns, ExpectedOutputs)
	global UnitsQuantity;
	global hiddenLayers;
	global maxError;
	global Outputs;
	global currentError;
	global Errors;


	inputUnits 		 = UnitsQuantity(1);
	inputSize		 = size(Patterns)(2);
	inputOrder 		 = shuffle(1 : inputSize, inputSize);
	acumError  		 = 0;
	analizedPatterns = 0;

	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		CurrentPattern  = CurrentPattern / norm(CurrentPattern); #normalize input
		Input 			= CurrentPattern;

		incrementalForwardStep(Input);

		ExpectedOutput = ExpectedOutputs(inputOrder(index));
		CurrOutput 	   = cell2mat(Outputs(hiddenLayers + 1));
		acumError = acumError + (ExpectedOutput - CurrOutput) ** 2;

		if(ExpectedOutput != CurrOutput)
			#calculate Deltas
			deltaCalculation(ExpectedOutput, CurrOutput);

			#update weights
			incrementalWeightUpdate(CurrentPattern);
		endif

		analizedPatterns = analizedPatterns + 1;

		if(mod(analizedPatterns, inputSize) == 0)
				currentError = acumError / inputSize
				Errors = [Errors currentError];
				plot(Errors);
				acumError = 0;
		endif
	endfor
endfunction