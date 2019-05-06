function deltaCalculation(ExpectedOutput, CurrOutput)
	global hiddenLayers;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;

	Deltas(hiddenLayers + 1) =  (ExpectedOutput - CurrOutput);

	for currentLayer = hiddenLayers : -1 : 1
		currentWeights	       = Weights{currentLayer + 1}; #get matrix
		currentWeights(:, [1]) = []; #remove bias weight
		currentWeights 		   = currentWeights';
		Deltas(currentLayer)   = gPrima(Outputs{currentLayer}).* (currentWeights * Deltas{currentLayer + 1});
	endfor
endfunction