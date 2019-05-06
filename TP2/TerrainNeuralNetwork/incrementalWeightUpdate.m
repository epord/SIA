function incrementalWeightUpdate(CurrentPattern)
	global hiddenLayers;
	global learningFactor;
	global Weights;
	global Deltas;
	global Outputs;

	for currentLayer = 1 : hiddenLayers + 1
		if(currentLayer == 1)
			DeltaWeights = learningFactor * Deltas{currentLayer} * (CurrentPattern');
		else
			OutputWithBias = Outputs{currentLayer - 1}'; #get matrix and transpose
			OutputWithBias = [-1, OutputWithBias]; #Add bias
			DeltaWeights = learningFactor * Deltas{currentLayer} * OutputWithBias;
		endif
		Weights(currentLayer) = Weights{currentLayer} + DeltaWeights;
	endfor
endfunction