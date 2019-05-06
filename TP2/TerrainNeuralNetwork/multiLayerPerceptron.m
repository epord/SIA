# Terrain Neural Network

1; #prevent it from being a function file
source("architecture.conf")
global Weights;
global Deltas;
global MembranePotentials;
global Outputs;
global currentError 	= 1;
global trainingQuantity = 4;
global Errors = [];
global EpsilonErrors = [];

function [TrainingPatterns, TrainingOutputs, TestPatterns, TestOutputs] = getPatterns(In, Out)
	global trainingPercentage;

	inputSize = size(In)(1);

	InputOrder = shuffle([1 : inputSize], inputSize);
	len 	   = floor(trainingPercentage * inputSize);
	for index = 1 : len
		TrainingPatterns(index, :) = In(InputOrder(index), :);
		TrainingOutputs(index)  = Out(InputOrder(index));
	endfor
	for index = len + 1 : inputSize
		TestPatterns(index - len, :) = In(InputOrder(index), :);
		TestOutputs(index - len)  = Out(InputOrder(index));
	endfor

	TrainingPatterns = TrainingPatterns';
	TestPatterns    = TestPatterns';
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


####################################################################################################
#------------------------------------->  start of code  <-------------------------------------######

initializeNeuralNetwork();

[In, Out] = readDataFile(dataFile);
In = [(zeros(size(In)(1), 1) -1) In];
[TrainingPatterns, TrainingOutputs, TestPatterns, TestOutputs] = getPatterns(In, Out);

epoch = 1;

do
	if(method == 0)
		incrementalTraining(TrainingPatterns, TrainingOutputs);
	elseif(method == 1)
		batchTraining(trainingPatterns, TrainingOutputs);
	else
		printf("Invalid method.\n");
		exit(1);
	endif

	epoch = epoch + 1;

until (currentError < maxError)


############################################## start of tests #########################################

inputSize = size(TestPatterns)(2);
failed = 0;

for index = 1 : inputSize
		CurrentPattern  = TestPatterns(:, index);
		incrementalForwardStep(CurrentPattern);

		ExpectedOutput = TestOutputs(index)
		CurrOutput 	   = cell2mat(Outputs(hiddenLayers + 1))
		printf("\n");

		if(abs(ExpectedOutput - CurrOutput) > maxError)
			failed = failed + 1;
		endif
endfor
failed
total = inputSize





