function adaptiveEtaFn()
    # Persistent variable maintains its value throughout repeated function calls, but is only visible inside this function
	persistent epochsCounter = 0; # Assume this is called once per epoch;
	global OldWeights;
	persistent adaptiveEtaModificationsCount = 0;
    # Globals
	global Errors;
	global currentError;
    global adaptiveEta;
	global adaptiveEtaDeltaSteps;
	global learningFactor;
	global learningFactorIncreaseConstant;
	global learningFactorDecreaseFactor;
	global Weights;
    global showPlot;
	global plotAdaptiveEtaPoints;
	global plotAdaptiveEtaLearningRate;
	global alphaMomentum; # Adaptive eta can affect momentum too
	persistent oldAlphaMomentum = alphaMomentum;

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
				printf("Decreased learning factor to %g\n", learningFactor);
				if (showPlot && plotAdaptiveEtaPoints)
					scatter(errorSize, currentError, 'r', 'x'); # Plot a red cross indicating learning factor decreased
					if (plotAdaptiveEtaLearningRate && mod(adaptiveEtaModificationsCount, adaptiveEtaDeltaSteps) == 0)
						text(errorSize, currentError * 1.1, num2str(learningFactor));
					endif
				endif
				Weights = OldWeights;
				epochsCounter = 0;
			elseif (deltaError < 0)
				alphaMomentum = oldAlphaMomentum;
				if (mod(epochsCounter, adaptiveEtaDeltaSteps) == 0)
					lastError = Errors(end);
					previousErrors = Errors(end-1 : -1 : end-adaptiveEtaDeltaSteps+1);
					if (all(previousErrors >= lastError))
						# Error has been consistently decreasing, increase learning factor to not be so conservative
						learningFactor += learningFactorIncreaseConstant;
						adaptiveEtaModificationsCount++;
						# printf("Increased learning factor to %g\n", learningFactor);
						if (showPlot && plotAdaptiveEtaPoints)
							scatter(errorSize, currentError, 'b', 'o'); # Plot a blue circle indicating learning factor increased
							if (plotAdaptiveEtaLearningRate && mod(adaptiveEtaModificationsCount, adaptiveEtaDeltaSteps) == 0)
								text(errorSize, currentError * 0.9, num2str(learningFactor));
							endif						
						endif
					endif
				endif
			endif
		endif
        epochsCounter++;
	endif
endfunction
