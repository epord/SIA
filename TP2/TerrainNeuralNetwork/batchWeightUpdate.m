function batchWeightUpdate(CurrentPattern)
	global hiddenLayers;
	global learningFactor;
	global Weights;
	global Deltas;
	global Outputs;

	for currentLayer = 1 : hiddenLayers + 1
		if(currentLayer == 1)

			DeltaWeights = learningFactor * cell2mat(Deltas(currentLayer)) * (CurrentPattern');
		else
			d = cell2mat(Deltas(currentLayer))
			p = CurrentPattern
			exit(1);
			OutputWithBias = cell2mat(Outputs(currentLayer - 1))'; #get matrix and transpose
			OutputWithBias = [-1, OutputWithBias]; #Add bias
			DeltaWeights = learningFactor * cell2mat(Deltas(currentLayer)) * OutputWithBias;
		endif
		Weights(currentLayer) = cell2mat(Weights(currentLayer)) + DeltaWeights;
	endfor
endfunction