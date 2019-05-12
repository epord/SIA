# Terrain Neural Network

1; #prevent it from being a function file

clear all; 	# Clear everything, including global variables
debug_on_interrupt(1);	# Enter debug mode on CTRL+C (works on Linux only)

source("architecture.conf")

global Weights;
global Deltas;
global MembranePotentials;
global Outputs;
global hiddenLayers = size(UnitsQuantity)(2) - 2; # calculate hiddenLayers quantity
global currentError = 1;
global trainingQuantity = 4;
global maxError = maxEpsilon ** 2 / 2
global Errors = [];
global DeltaWeights;
global OldDeltaWeights;
global UnitsQuantity;
global maxEpochs;

# Configurations by program arguments
global silent = false;
global keyToExit = true;
global showPlot = true;

function processProgramArgument(argument)
	global silent;
	global keyToExit;
	global showPlot;

	if strcmp(argument, "--silent")
		silent = true;
	elseif strcmp(argument, "--auto-exit")
		keyToExit = false;
	elseif strcmp(argument, "--no-plot")
		showPlot = false;
	endif
endfunction

# Processing program arguments
for i = 1 : size(argv)(1)
	processProgramArgument(argv(){i});
endfor

if (showPlot)
	clf;		# Clear plotting area
	hold on;	# Allow plotting multiple things
endif

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
	global DeltaWeights;
	global OldDeltaWeights;
	WeightMax = zeros(1, hiddenLayers +1);

	if(size(UnitsQuantity)(2) == hiddenLayers + 2)
		Weights         = cell(hiddenLayers + 1, 1);
		DeltaWeights    = cell(hiddenLayers + 1, 1);
		OldDeltaWeights = cell(hiddenLayers + 1, 1);
		for layer = 1 : hiddenLayers + 1;
			#setting max weight according to method rand or fan-in
			if(weightInitMethod == 1)
				WeightMax(layer) = 1 / sqrt(UnitsQuantity(layer) + 1); #fan-in
			else
				WeightMax(layer) = randAbsolut;
			endif

			Weights(layer)		   = ((rand(UnitsQuantity(layer + 1), UnitsQuantity(layer) + 1) - 0.5) * 2 * WeightMax(layer));
			DeltaWeights(layer)	   = zeros(UnitsQuantity(layer + 1), UnitsQuantity(layer) + 1);
			OldDeltaWeights(layer) = zeros(UnitsQuantity(layer + 1), UnitsQuantity(layer) + 1);

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
	global beta;

	if(activationFunction == 0)
		result = tanh(beta * input);
	elseif(activationFunction == 1)
		result = 1.0 ./ (1.0 + exp(-2 * beta * input));
	elseif(activationFunction == 2)
		result = input; #eye linear function
	else
		printf("activation Function not implemented\n");
		exit(1);
	endif
endfunction

function [result] = gPrima(output)
	global activationFunction;
	global beta;

	if(activationFunction == 0)
		result = beta * (1 - output.** 2); #tanh derivative
	elseif(activationFunction == 1)
		result = 2 * beta * output .* (1 - output); #exp derivative
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
		batchTraining(TrainingPatterns, TrainingOutputs);
	else
		printf("Invalid method.\n");
		exit(1);
	endif

	epoch = epoch + 1;

until (currentError < maxError || epoch > maxEpochs)

############################################## start of tests #########################################

ProvidedPatterns = [TrainingPatterns TestPatterns];
ProvidedOutputs = [TrainingOutputs TestOutputs];

inputSize = size(ProvidedPatterns)(2);
failed = 0;

for index = 1 : inputSize
		CurrentPattern  = ProvidedPatterns(:, index);
		CurrentPattern  = CurrentPattern / norm(CurrentPattern); #normalize input
		incrementalForwardStep(CurrentPattern);

		ExpectedOutput = ProvidedOutputs(index);
		CurrOutput 	   = Outputs{hiddenLayers + 1};

		if(abs(ExpectedOutput - CurrOutput) > maxEpsilon)
			failed = failed + 1;
			if (!silent)
				printf("%sFAILED --- Expected: %+.5f   ||   Obtained: %+.5f %s\n", "\x1B[31m", ExpectedOutput, CurrOutput, "\x1B[0m")
			endif
		else
			if (!silent)
				printf("%s  OK   --- Expected: %+.5f   ||   Obtained: %+.5f %s\n", "\x1B[32m", ExpectedOutput, CurrOutput, "\x1B[0m")
			endif
		endif
endfor

printf("\n============================================================\n")
UnitsQuantity
Weights
printf('Error was %.5f after %d epochs\n', currentError, epoch)
printf('Failed %d/%d\n', failed, inputSize)
printf("\n============================================================\n")


############################################## terrain generation #########################################

inputSize = 10000;
Positions = [(ones(1,inputSize) * -1); (rand(2, inputSize) * 6 - 3)];
plotOutputs = [];

for index = 1 : inputSize
	CurrentPattern  = Positions(:, index);
	CurrentPattern  = CurrentPattern / norm(CurrentPattern); #normalize input
	incrementalForwardStep(CurrentPattern);
	plotOutputs = [plotOutputs Outputs{hiddenLayers + 1}];
endfor

# Save errors plot
figure(1)
xlabel("Epochs")
ylabel("Mean Squared Error")
title("Mean Squared Error")
filename = strcat("plots/errors-", num2str(UnitsQuantity));
filename = strrep(filename, " ", "_");
print(filename, "-dsvg")

# Save generated terrain positions plot
figure(2);
plot3(Positions(2,:), Positions(3,:), plotOutputs, ".", "color", "blue")
title ("Generated terrain positions");
axis([-3 3 -3 3 -1 1]);
xlabel("X")
ylabel("Y")
zlabel("Z (network output)")
filename = strcat("plots/providedPoints-", num2str(UnitsQuantity));
filename = strrep(filename, " ", "_");
print(filename, "-dsvg")

hold on

# Save generated AND provided terrain positions plot
plot3(TrainingPatterns(2,:), TrainingPatterns(3,:), TrainingOutputs, "*", "color", "red")
axis([-3 3 -3 3 -1 1]);
title ("Provided terrain positions");
xlabel("X")
ylabel("Y")
zlabel("Z")
filename = strcat("plots/both-", num2str(UnitsQuantity));
filename = strrep(filename, " ", "_");
print(filename, "-dsvg")



if (keyToExit)
	printf("\nPress any key to exit\n")
	kbhit();
endif
