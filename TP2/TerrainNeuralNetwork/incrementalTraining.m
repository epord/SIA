function incrementalTraining(Patterns, ExpectedOutputs)
	# Persistent variable maintains its value throughout repeated function calls, but is only visible inside this function
	persistent epochsCounter = 0;
	persistent OldWeights;

	global UnitsQuantity;
	global hiddenLayers;
	global maxError;
	global Outputs;
	global currentError;
	global Errors;
	# Adaptive eta
	global adaptiveEta;
	global adaptiveEtaDeltaSteps;
	global learningFactor;
	global learningFactorIncreaseConstant;
	global learningFactorDecreaseFactor;
	global Weights;
	global alphaMomentum; # Adaptive eta can affect momentum too


	inputUnits 		 = UnitsQuantity(1);
	inputSize		 = size(Patterns)(2);
	inputOrder 		 = shuffle(1 : inputSize, inputSize);
	acumError  		 = 0;
	accumEpsilon	 = 0;
	analizedPatterns = 0;

	if (adaptiveEta)
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

		analizedPatterns++;

		# if(mod(analizedPatterns, inputSize) == 0)
		# 		currentError = acumError / (2*inputSize)
		# 		Errors = [Errors currentError];
		# 		plot (Errors);
		# 		acumError = 0;
		# endif
	endfor
	
	epochsCounter++;

	currentError = acumError / (2*inputSize)
	Errors = [Errors currentError];
	plot(Errors);
	acumError = 0;

	if (adaptiveEta)
		if (size(Errors)(2) >= 2)
			deltaError = Errors(end) - Errors(end-1)
			if (deltaError > 0)
				# Error increased, decrease learning factor
				learningFactor -= learningFactor * learningFactorDecreaseFactor;
				# Reset momentum (no effect if momentum is disabled)
				alphaMomentum = 0;
				
				Weights = OldWeights;
				epochsCounter = 0;
			elseif (deltaError < 0 && mod(epochsCounter, adaptiveEtaDeltaSteps) == 0)
				lastError = Errors(end);
				previousErrors = Errors(end-1 : -1 : end-adaptiveEtaDeltaSteps);
				if (all(previousErrors >= lastError))
					# Error has been consistently decreasing, increase learning factor to not be so conservative
					learningFactor += learningFactorIncreaseConstant;
				endif
			endif
		endif
	endif

endfunction