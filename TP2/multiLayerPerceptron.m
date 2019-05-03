# XOR neural network

1; #prevent it from being a function file
source("architecture.conf")
global Weights;
global Deltas;
global MembranePotentials;
global Outputs;
global currentError = 1;


function generateWeights()
	global Weights;
	global hiddenLayers;
	global UnitsQuantity;
	global randAbsolut;
	global weightInitMethod;
	
	if(size(UnitsQuantity)(2) == hiddenLayers + 2)
		Weights = cell(hiddenLayers + 1, 1);
	
		for layer = 1 : hiddenLayers + 1
			if(weightInitMethod == 0)
				Weights(layer) = ((rand(UnitsQuantity(layer + 1), UnitsQuantity(layer) + 1) - 0.5) * 2 *randAbsolut);
			endif
		endfor
	else
		printf("Invalid configuration for architecture hiddenLayers does not match UnitsQuantity\n");
		exit(1);
	endif
endfunction

function generateStructureForLayers()
	global Deltas;
	global MembranePotentials;
	global Outputs;
	global hiddenLayers;

	Deltas             = cell(hiddenLayers + 1, 1);
	MembranePotentials = cell(hiddenLayers + 1, 1);
	Outputs            = cell(hiddenLayers + 1, 1);
endfunction

function initializeNeuralNetwork()
	generateWeights();
	generateStructureForLayers();
endfunction

function pattern = getPatterns(N)

	if(N == 2)
		pattern = [-1, -1,  -1, -1; #Each column is a pattern. The first row is the bias (always -1)
			  		1,  1,  -1, -1; #NOTE: Outputs are not part of the patterns
			  		1, -1,   1, -1];
	elseif(N == 3)
		pattern = [-1, -1,  -1, -1, -1, -1,  -1,  -1;
			  		1,  1,  -1, -1,  1,  1,  -1,  -1;
			  		1, -1,   1, -1,  1, -1,   1,  -1;
			  		1,  1,   1,  1, -1, -1,  -1,  -1];
	
	elseif(N == 4)
		pattern = [-1, -1,  -1, -1, -1, -1,  -1,  -1, -1, -1,  -1, -1, -1, -1,  -1,  -1;
			  		1,  1,  -1, -1,  1,  1,  -1,  -1,  1,  1,  -1, -1,  1,  1,  -1,  -1;
			  		1, -1,   1, -1,  1, -1,   1,  -1,  1, -1,   1, -1,  1, -1,   1,  -1;
			  		1,  1,   1,  1, -1, -1,  -1,  -1,  1,  1,   1,  1, -1, -1,  -1,  -1;
			  		1,  1,   1,  1,  1,  1,   1,   1, -1, -1,  -1, -1, -1, -1,  -1,  -1];
	elseif(N == 5)
		pattern = [-1, -1,  -1, -1, -1, -1,  -1,  -1, -1, -1,  -1, -1, -1, -1,  -1,  -1, -1, -1,  -1, -1, -1, -1,  -1,  -1, -1, -1,  -1, -1, -1, -1,  -1,  -1;
			  		1,  1,  -1, -1,  1,  1,  -1,  -1,  1,  1,  -1, -1,  1,  1,  -1,  -1,  1,  1,  -1, -1,  1,  1,  -1,  -1,  1,  1,  -1, -1,  1,  1,  -1,  -1;
			  		1, -1,   1, -1,  1, -1,   1,  -1,  1, -1,   1, -1,  1, -1,   1,  -1,  1, -1,   1, -1,  1, -1,   1,  -1,  1, -1,   1, -1,  1, -1,   1,  -1;
			  		1,  1,   1,  1, -1, -1,  -1,  -1,  1,  1,   1,  1, -1, -1,  -1,  -1,  1,  1,   1,  1, -1, -1,  -1,  -1,  1,  1,   1,  1, -1, -1,  -1,  -1;
			  		1,  1,   1,  1,  1,  1,   1,   1, -1, -1,  -1, -1, -1, -1,  -1,  -1,  1,  1,   1,  1,  1,  1,   1,   1, -1, -1,  -1, -1, -1, -1,  -1,  -1;
			  		1,  1,   1,  1,  1,  1,   1,   1,  1,  1,   1,  1,  1,  1,   1.   1, -1, -1,  -1, -1, -1, -1,  -1,  -1, -1, -1,  -1, -1, -1, -1,  -1,  -1];
	else

		printf("Invalid parameter: %d\n", N);
		exit(1);
	endif
endfunction

function newVector = shuffle(vector, vectorSize)
	newVector = vector;
	for index = 1 : vectorSize
			newIndex = floor(rand() * vectorSize + 1);
			aux = newVector(newIndex);
			newVector(newIndex) = newVector(index);
			newVector(index) = aux;
		endfor
endfunction

function [result] = g(input) 
	global activationFunction;

	if(activationFunction == 0)
		result = tanh(input);
	elseif(activationFunction == 1)
		result = 1.0 ./ (1.0 + exp(-input));
	elseif(activationFunction == 2)
		result = input; #eye linear function
	else
		printf("activation Function not implemented\n");
		exit(1);
	endif
endfunction

function [result] = gPrima(output)
	global activationFunction;

	if(activationFunction == 0)
		result = 1 - output.** 2; #tanh derivative
	elseif(activationFunction == 1)
		result = output .* (1 - output); #exp derivative
	elseif(activationFunction == 2)
		result = 1; #linear derivative is constant
	else
		printf("activation Function not implemented\n");
		exit(1);
	endif
endfunction

function incrementalTraining(Patterns, ExpectedOutputs)
	global UnitsQuantity;
	global hiddenLayers;
	global maxError;
	global Outputs;
	inputUnits 		 = UnitsQuantity(1);
	inputSize  		 = 2 ** inputUnits;
	inputOrder 		 = shuffle(1 : inputSize, inputSize);
	acumError  		 = 0;
	analizedPatterns = 0;

	
	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		CurrentPattern  = CurrentPattern / norm(CurrentPattern); #normalize input
		Input 			= CurrentPattern; #comment

		incrementalForwardStep(Input);

		ExpectedOutput = ExpectedOutputs(inputOrder(index)); #TODO
		CurrOutput 	   = cell2mat(Outputs(hiddenLayers + 1));	
		acumError = acumError + (ExpectedOutput - CurrOutput) ** 2; #comment

		if(ExpectedOutput != CurrOutput)
			#calculate Deltas
			incrementalDeltaCalculation(ExpectedOutput, CurrOutput);
			
			#update weights 
			incrementalWeightUpdate(CurrentPattern);
		endif

		analizedPatterns = analizedPatterns + 1; 

		if(mod(analizedPatterns, 4) == 0)
				currentError = acumError / 2#comment
				acumError = 0;
		endif 
	endfor
endfunction

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
				Input  = [-1; Output]; #comment
				Input  = Input / norm(Input); #normalize output
			endif
		endfor 
endfunction

function incrementalWeightUpdate(CurrentPattern)
	global hiddenLayers;
	global learningFactor;
	global Weights;
	global Deltas;
	global Outputs;

	for currentLayer = 1 : hiddenLayers + 1
		if(currentLayer == 1)
			DeltaWeights = learningFactor * cell2mat(Deltas(currentLayer)) * (CurrentPattern');#comment
		else
			OutputWithBias = cell2mat(Outputs(currentLayer - 1))'; #get matrix and transpose
			OutputWithBias = [-1, OutputWithBias]; #Add bias 
			DeltaWeights = learningFactor * cell2mat(Deltas(currentLayer)) * OutputWithBias;#comment
		endif
		Weights(currentLayer) = cell2mat(Weights(currentLayer)) - DeltaWeights;
	endfor

endfunction

function incrementalDeltaCalculation(ExpectedOutput, CurrOutput)
	global hiddenLayers;
	global Weights;
	global Deltas;
	global MembranePotentials;

	Deltas(hiddenLayers + 1) = gPrima(cell2mat(MembranePotentials(hiddenLayers + 1))) .* (ExpectedOutput - CurrOutput);
			
	for currentLayer = hiddenLayers : -1 : 1
		currentWeights	       = cell2mat(Weights(currentLayer + 1)); #get matrix
		currentWeights(:, [1]) = []; #remove bias weight
		currentWeights 		   = currentWeights';#comment
		Deltas(currentLayer)   = gPrima(cell2mat(MembranePotentials(currentLayer))).* (currentWeights * cell2mat(Deltas(currentLayer + 1)));
	endfor
endfunction

function batchTraining(Patterns, ExpectedOutputs)
	global UnitsQuantity;
	global hiddenLayers;
	global learningFactor;
	global maxError;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;
	printf("method not implemented\n");
	exit(1);
endfunction

####################################################################################################
#------------------------------------->  start of code  <-------------------------------------######

initializeNeuralNetwork();

epoch			= 1;
ExpectedOutputs = [-1, 1, 1, -1]; #hardcoded for inputUnits = 2
Patterns 		= getPatterns(inputUnits);

do 	
	epoch; #comment
	
	if(method == 0)
		incrementalTraining(Patterns, ExpectedOutputs);
	elseif(method == 1)
		batchTraining(Patterns, ExpectedOutputs);
	else
		printf("Invalid method.\n");
		exit(1);
	endif
	
	epoch = epoch + 1;

until (currentError < maxError)




