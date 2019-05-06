function batchForwardStep(Patterns)
	global hiddenLayers;
	global Weights;
	global Outputs;
	global MembranePotentials;
	inputSize = size(Patterns)(2);

	#Maybe Normalize Patterns
	#Patterns = normalizePatterns(Patters)
	Input = Patterns;
	
	for currentLayer = 1 : hiddenLayers + 1
		Output = Weights{currentLayer} * Input;
		MembranePotentials(currentLayer) = Output;
		
		if(currentLayer != hiddenLayers + 1)
			Output = g(Output); #Linear function to output layer
		endif

		Outputs(currentLayer) = Output;

		if(currentLayer <= hiddenLayers)
			Input  = [zeros(1, inputSize) - 1; Output];
		endif
	endfor
endfunction