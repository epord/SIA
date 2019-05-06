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
			Outputs(currentLayer) = Output / norm(Output); #normalize output
		else
			Outputs(currentLayer) = Output;
		endif

		if(currentLayer <= hiddenLayers)
			Input  = [-1; Output];
			Input  = Input / norm(Input); #normalize output
		endif
	endfor

endfunction