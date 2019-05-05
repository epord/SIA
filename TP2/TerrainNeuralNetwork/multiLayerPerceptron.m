# Terrain Neural Network

1; #prevent it from being a function file
source("architecture.conf")
global Weights;
global Deltas;
global MembranePotentials;
global Outputs;
global currentError 	= 1;
global trainingQuantity = 4;

function [TrainingPatterns, TrainingOutputs, TestPatterns, TestOutputs] = getPatterns(In, Out)
	global trainingPercentage;

	inputSize = size(In)(1);

	InputOrder = shuffle([1 : inputSize], inputSize);
	len 	   = floor(trainingPercentage * inputSize);

	for index = 1 : len
		TrainingPatterns(index) = In(InputOrder(index));
		TrainingOutputs(index)  = Out(InputOrder(index));
	endfor
	for index = len + 1 : inputSize
		TestPatterns(index - len) = In(InputOrder(index));
		TestOutputs(index - len)  = Out(InputOrder(index));
	endfor
endfunction

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
	global method;
	global trainingQuantity;

	Deltas             = cell(hiddenLayers + 1, 1);
	MembranePotentials = cell(hiddenLayers + 1, 1);
	Outputs            = cell(hiddenLayers + 1, 1);
	
endfunction

function initializeNeuralNetwork()
	generateWeights();
	generateStructureForLayers();
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
	global currentError;

	inputUnits 		 = UnitsQuantity(1);
	inputSize  		 = 2 ** inputUnits;
	inputOrder 		 = shuffle(1 : inputSize, inputSize);
	acumError  		 = 0;
	analizedPatterns = 0;

	
	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		#CurrentPattern  = CurrentPattern / norm(CurrentPattern); #normalize input
		Input 			= CurrentPattern; #comment

		incrementalForwardStep(Input);

		ExpectedOutput = ExpectedOutputs(inputOrder(index)); #TODO
		CurrOutput 	   = cell2mat(Outputs(hiddenLayers + 1));	
		acumError = acumError + (ExpectedOutput - CurrOutput) ** 2; #comment

		if(ExpectedOutput != CurrOutput)
			#calculate Deltas
			deltaCalculation(ExpectedOutput, CurrOutput);
			
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
			Outputs(currentLayer) = Output;# / norm(Output); #normalize output 
		else
			Outputs(currentLayer) = Output;
		endif

		if(currentLayer <= hiddenLayers)
			Input  = [-1; Output]; #comment
			#Input  = Input / norm(Input); #normalize output
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
		Weights(currentLayer) = cell2mat(Weights(currentLayer)) + DeltaWeights;
	endfor	
endfunction

function deltaCalculation(ExpectedOutput, CurrOutput)
	global hiddenLayers;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;

	Deltas(hiddenLayers + 1) = gPrima(cell2mat(Outputs(hiddenLayers + 1))) .* (ExpectedOutput - CurrOutput);
			
	for currentLayer = hiddenLayers : -1 : 1
		currentWeights	       = cell2mat(Weights(currentLayer + 1)); #get matrix
		currentWeights(:, [1]) = []; #remove bias weight
		currentWeights 		   = currentWeights';#comment
		Deltas(currentLayer)   = gPrima(cell2mat(Outputs(currentLayer))).* (currentWeights * cell2mat(Deltas(currentLayer + 1)));
	endfor
endfunction

function batchTraining(Patterns, ExpectedOutputs)
	global hiddenLayers;
	global maxError;
	global Weights;
	global Deltas;
	global MembranePotentials;
	global Outputs;

	
	batchForwardStep(Patterns);

	#calculate Deltas
	deltaCalculation(ExpectedOutputs, cell2mat(Outputs(hiddenLayers + 1)));

	#update weights 
	batchWeightUpdate(Patterns);
		
	currentError = acumError / 2#comment
	printf("method not implemented\n");
endfunction

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
			Input  = [zeros(1, inputSize) - 1; Output]; #comment
		endif
	endfor
endfunction

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






####################################################################################################
#------------------------------------->  start of code  <-------------------------------------######

initializeNeuralNetwork();

[Inputs, Outputs] = readDataFile(dataFile);
[trainingPatterns,trainingOutputs, testPatterns, testOutput] = getPatterns(Inputs,Outputs)

exit(1)

epoch			= 1;
ExpectedOutputs = [0, 1, 1, 0]; #hardcoded for inputUnits = 2

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






