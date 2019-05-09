function incrementalWeightUpdate(CurrentPattern)
	global hiddenLayers;
	global learningFactor;
	global Weights;
	global OldWeights;
	global Deltas;
	global Outputs;
	global momentum;
	global alphaMomentum;
	global DeltaWeights;
	global OldDeltaWeights; 
	# Adaptive eta
	global Errors;
	global adaptiveEta;
	global adaptiveEtaDeltaSteps;
	global learningFactorIncreaseConstant;
	global learningFactorDecreaseFactor;

	if (adaptiveEta) # 1 is truthy
		deltaError = Errors(end) - Errors(end-1)
		if (deltaError > 0)
			# Error increased, decrease learning factor
			learningFactor -= learningFactor * learningFactorDecreaseFactor;
			# Reset momentum (no effect if momentum is disabled)
			alphaMomentum = 0;
			# Revert weights to those K steps back
			Weights = OldWeights(end-adaptiveEtaDeltaSteps+1); # TODO: Verify end-adaptiveEtaDeltaSteps+1 == 1 always?
			# Clear memory of old weights
			OldWeights = cell(hiddenLayers + 1, 1);
			return;
		elseif (deltaError < 0 && size(Errors)(2) >= adaptiveEtaDeltaSteps)
			lastError = Errors(end);
			previousErrors = Errors(end-1 : -1 : end-adaptiveEtaDeltaSteps+1);
			if (all(previousErrors >= lastError))
				# Error has been consistently decreasing, increase learning factor to not be so conservative
				learningFactor += learningFactorIncreaseConstant;
			endif
		endif
	endif

	OldWeights = [OldWeights Weights];

	for currentLayer = 1 : hiddenLayers + 1
		if(currentLayer == 1)
			DeltaWeights(currentLayer) = learningFactor * Deltas{currentLayer} * (CurrentPattern');
		else
			OutputWithBias 			   = Outputs{currentLayer - 1}'; #get matrix and transpose
			OutputWithBias 			   = [-1, OutputWithBias]; #Add bias
			DeltaWeights(currentLayer) = learningFactor * Deltas{currentLayer} * OutputWithBias;
		endif

		if(momentum == 1)
				DeltaWeights(currentLayer) = DeltaWeights{currentLayer} + alphaMomentum * OldDeltaWeights{currentLayer};
				OldDeltaWeights(currentLayer)   = DeltaWeights{currentLayer};   
		endif

		Weights(currentLayer) = Weights{currentLayer} + DeltaWeights{currentLayer};
	endfor
endfunction