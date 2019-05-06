function batchWeightUpdate(CurrentPattern)
	global hiddenLayers;
	global learningFactor;
	global Weights;
	global Deltas;
	global Outputs;
	global DeltaWeights;
	global OldDeltaWeights; 
	global momentum;
	global alphaMomentum;

	for currentLayer = 1 : hiddenLayers + 1
		if(currentLayer == 1)
			DeltaWeights(currentLayer) = learningFactor * Deltas{currentLayer} * (CurrentPattern');
		else
			outputs = size(Outputs{currentLayer})
			OutputWithBias 			   = Outputs{currentLayer - 1}'; #get matrix and transpose
			Bias 					   = zeros(size(OutputWithBias)(1)) - 1;
			OutputWithBias 			   = [Bias OutputWithBias]; #Add bias
			Delts = size(Deltas{currentLayer})
			OutputBias = size(OutputWithBias)
			DW = size(DeltaWeights{currentLayer})
			W = size(Weights{currentLayer})

			DeltaWeights(currentLayer) = learningFactor * Deltas{currentLayer} * OutputWithBias;
		endif

		if(momentum == 1)
				DeltaWeights(currentLayer) = DeltaWeights{currentLayer} + alphaMomentum * OldDeltaWeights{currentLayer};
				OldDeltaWeights(currentLayer)   = DeltaWeights{currentLayer};   
		endif

		Weights(currentLayer) = Weights{currentLayer} + DeltaWeights{currentLayer};
	endfor
endfunction