function batchForwardStep(Patterns)
	global hiddenLayers;
	global Weights;
	global Outputs;
	global MembranePotentials;
	Input = Patterns;
	inputSize = size(Patterns)(2);

	for currentLayer = 1 : hiddenLayers + 1
		Output = cell2mat(Weights(currentLayer)) * Patterns;
		MembranePotentials(currentLayer) = Output;
		Output = g(Output);
		Outputs(currentLayer) = Output;

		if(currentLayer <= hiddenLayers)
			Input  = [zeros(1, inputSize) - 1; Output];
		endif
	endfor
endfunction