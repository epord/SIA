function batchTraining(Patterns, ExpectedOutputs)
	global hiddenLayers;
	global maxError;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;
	global Errors;
	global currentError;
	global silent;
	global showPlot;
	# Adaptive eta
	global adaptiveEta;
	global Weights;
	global OldWeights;

	if (adaptiveEta)
		OldWeights = Weights;
	endif

	Patterns = normalizePatterns(Patterns);
	batchForwardStep(Patterns);

	# Calculate Deltas
	deltaCalculation(ExpectedOutputs, Outputs{hiddenLayers + 1});
	
	# Update weights
	batchWeightUpdate(Patterns);

	# Update errors
	CurrentBatchError = (ExpectedOutputs - Outputs{hiddenLayers + 1}) .** 2;
	currentError = sum(CurrentBatchError) / (2 * size(Patterns)(2));
	Errors = [Errors currentError];

	# Update errors
	if (!silent)
		currentError
	endif
	if (showPlot)
		figure(1)
		plot(Errors);
	endif

	if (adaptiveEta)
		adaptiveEtaFn()
	endif
	
endfunction

function [colNorms] = normalizePatterns(Patterns)
	colNorms = zeros(size(Patterns)(1), size(Patterns)(2));

	for col = 1 : size(Patterns)(2)
		currInput = Patterns(: , col);
		colNorms(:, col) = currInput / norm(currInput);
	endfor
endfunction