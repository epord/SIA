function incrementalForwardStep(Input)
	global hiddenLayers;
	global Weights;
	global MembranePotentials;
	global Outputs;


	for currentLayer = 1 : hiddenLayers + 1
		Output = cell2mat(Weights(currentLayer)) * Input;
		MembranePotentials(currentLayer) = Output;
		Output = g(Output);
		if(currentLayer != hiddenLayers + 1)
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