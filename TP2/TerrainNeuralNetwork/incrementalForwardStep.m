function incrementalForwardStep(Input)
	global hiddenLayers;
	global Weights;
	global MembranePotentials;
	global Outputs;

	for currentLayer = 1 : hiddenLayers + 1
		Output = Weights{currentLayer} * Input;
		MembranePotentials(currentLayer) = Output;

		if(currentLayer != hiddenLayers + 1)
			Output = g(Output);
			Outputs(currentLayer) = Output;
		else
			Outputs(currentLayer) = Output; # Linear function in output layer
		endif

		if(currentLayer <= hiddenLayers)
			Input  = [-1; Output];
		endif
	endfor

endfunction