# XOR neural network

1;

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
		pattern = zeros(N, N);
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
	#result = tanh(input);
	result = 1.0 ./ (1.0 + exp(-input));
endfunction

#Where output is tanh(input)
function [result] = gPrima(output)
	#result = 1 - output.** 2;
	result = output .* (1 - output);
endfunction

####################################################################################################
#start of code


hiddenLayers 	= 1;
hiddenUnits  	= 2;
outputUnits  	= 1;
inputUnits   	= 2;
currentError 	= 1;
maxError		= 0.005;
learningFactor  = 0.1;
epoch			= 1;
acumError		= 0;


#Generate weights TODO: more generic with function jejeje
Weights 		= cell(hiddenLayers + 1, 1);
Weights(1)		= (rand(hiddenUnits, inputUnits + 1) - 0.5);
Weights(2)		= (rand(outputUnits, hiddenUnits + 1) - 0.5);

ExpectedOutputs = [0, 1, 1, 0]; #hardcoded for inputUnits = 2

Outputs 		= cell(hiddenLayers + 1, 1);
MembranePotentials = cell(hiddenLayers + 1, 1);
Deltas 			= cell(hiddenLayers + 1, 1);

inputSize		= 2 ** inputUnits;
Patterns 		= getPatterns(inputUnits);
analizedPatterns = 0;

do 	
	epoch
	inputOrder = shuffle(1 : inputSize, inputSize);
	
	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		Input 			= CurrentPattern #comment
		
		for currentLayer = 1 : hiddenLayers + 1
			Output = cell2mat(Weights(currentLayer)) * Input;
			MembranePotentials(currentLayer) = Output;
			Output = g(Output);
			Outputs(currentLayer) = Output;
			
			if(currentLayer <= hiddenLayers)
				Input  = [-1; Output] #comment
			endif
		endfor 
	
		ExpectedOutput = ExpectedOutputs(inputOrder(index)) #TODO
		currOutput 	   = cell2mat(Outputs(hiddenLayers + 1))
		
		if(ExpectedOutput != currOutput)
			acumError = acumError + (ExpectedOutput - cell2mat(Outputs(hiddenLayers + 1))) ** 2;#comment

			#calculateDeltas
			Deltas(hiddenLayers + 1) = gPrima(cell2mat(MembranePotentials(hiddenLayers + 1))) .* (ExpectedOutput - currOutput);
			
			for currentLayer = hiddenLayers : -1 : 1
				currentWeights	       = cell2mat(Weights(currentLayer + 1)); #get matrix
				currentWeights(:, [1]) = []; #remove bias weight
				currentWeights 		   = currentWeights';#comment
				Deltas(currentLayer)   = gPrima(cell2mat(MembranePotentials(currentLayer))).* (currentWeights * cell2mat(Deltas(currentLayer + 1)));#comment
			endfor
			printf("\n\n");
			Weights
			printf("\n\n");
			printf("\n\n");
			Deltas
			printf("\n\n");
		
			#update weights 
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
			analizedPatterns = analizedPatterns + 1;
			if(mod(index, 4) == 0)
				currentError = acumError / 2#comment
				acumError = 0;
			endif  
		endif
	endfor
	epoch = epoch + 1;

until (currentError < maxError  || epoch > 1000)




