function incrementalTraining(Patterns, ExpectedOutputs)
	# Persistent variable maintains its value throughout repeated function calls, but is only visible inside this function
	persistent epochsCounter = 0;
	persistent OldWeights;
	persistent adaptiveEtaModificationsCount = 0;

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
	global adaptiveEtaDeltaSteps;
	global learningFactor;
	global learningFactorIncreaseConstant;
	global learningFactorDecreaseFactor;
	global Weights;
	global plotAdaptiveEtaPoints;
	global plotAdaptiveEtaLearningRate;
	global alphaMomentum; # Adaptive eta can affect momentum too

	inputSize		 = size(Patterns)(2);
	inputOrder 		 = shuffle(1 : inputSize, inputSize);
	acumError  		 = 0;
	accumEpsilon	 = 0;

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
	endfor
	
	epochsCounter++;

	# Update errors
	currentError = acumError / (2*inputSize);
	Errors = [Errors currentError];
	if (!silent)
		currentError
	endif
	if (showPlot)
		plot(Errors);
	endif
	acumError = 0;

	if (adaptiveEta)
		errorSize = size(Errors)(2);
		if (errorSize >= 2) # Can't compare errors if there aren't at least 2 error records
			deltaError = Errors(end) - Errors(end-1);
			if (deltaError > 0)
				# Error increased, decrease learning factor
				learningFactor -= learningFactor * learningFactorDecreaseFactor;
				adaptiveEtaModificationsCount++;
				# Reset momentum (no effect if momentum is disabled)
				alphaMomentum = 0;
				# printf("Decreased learning factor to %g\n", learningFactor);
				if (plotAdaptiveEtaPoints)
					scatter(errorSize, currentError, 'r', 'x'); # Plot a red cross indicating learning factor decreased
					if (plotAdaptiveEtaLearningRate && mod(adaptiveEtaModificationsCount, adaptiveEtaDeltaSteps) == 0)
						text(errorSize, currentError * 1.1, num2str(learningFactor));
					endif
				endif
				Weights = OldWeights;
				epochsCounter = 0;
			elseif (deltaError < 0 && mod(epochsCounter, adaptiveEtaDeltaSteps) == 0)
				lastError = Errors(end);
				previousErrors = Errors(end-1 : -1 : end-adaptiveEtaDeltaSteps+1);
				if (all(previousErrors >= lastError))
					# Error has been consistently decreasing, increase learning factor to not be so conservative
					learningFactor += learningFactorIncreaseConstant;
					adaptiveEtaModificationsCount++;
					# printf("Increased learning factor to %g\n", learningFactor);
					if (plotAdaptiveEtaPoints)
						scatter(errorSize, currentError, 'b', 'o'); # Plot a blue circle indicating learning factor increased
						if (plotAdaptiveEtaLearningRate && mod(adaptiveEtaModificationsCount, adaptiveEtaDeltaSteps) == 0)
							text(errorSize, currentError * 0.9, num2str(learningFactor));
						endif						
					endif
				endif
			endif
		endif
	endif

endfunction