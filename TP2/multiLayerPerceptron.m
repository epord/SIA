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

function result = g(input) 
	result = tanh(input);
endfunction

#Where output is tanh(input)
function result = gPrima(output)
	result = 1 - output.** 2;
endfunction

####################################################################################################
#start of code


hiddenLayers 	= 1;
hiddenUnits  	= 2;
outputUnits  	= 1;
inputUnits   	= 2;
currentError 	= 1;
maxError		= 0.005;
learningFactor  = 0.4;
epoch			= 1;
acumError		= 0;


#Generate weights TODO: more generic with function jejeje
Weights 		= cell(hiddenLayers + 1, 1);
Weights(1)		= (rand(hiddenUnits, inputUnits + 1) - 0.5);
Weights(2)		= (rand(outputUnits, hiddenUnits + 1) - 0.5);

ExpectedOutputs = [-1, 1, 1, -1]; #hardcoded for inputUnits = 2

Outputs 		= cell(hiddenLayers + 1, 1);
MembranePotentials = cell(hiddenLayers + 1, 1);
Deltas 			= cell(hiddenLayers + 1, 1);

inputSize		= 2 ** inputUnits;
Patterns 		= getPatterns(inputUnits);
# Output 			= 0;

do 	
	inputOrder = shuffle(1 : inputSize, inputSize);
	
	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		Input 			= CurrentPattern 
		
		for currentLayer = 1 : hiddenLayers + 1
			Output = cell2mat(Weights(currentLayer)) * Input;
			MembranePotentials(currentLayer) = Output;
			currentLayer
			Output = g(Output);
			if(currentLayer == hiddenLayers + 1)
				Output = sign(Output);
			endif
			Output
			Outputs(currentLayer) = Output;

			
			if(currentLayer <= hiddenLayers)
				Input  = [-1; Output]
			endif
		endfor 
	
		#if output differs
		#fix weights and backpropagation
		ExpectedOutput = ExpectedOutputs(inputOrder(index)); #TODO
		ExpectedOutput #comment
		currOutput = cell2mat(Outputs(hiddenLayers + 1))
		
		if(ExpectedOutput != currOutput)
			acumError = (ExpectedOutput - cell2mat(Outputs(hiddenLayers + 1))) ** 2


			#calculateDeltas
			Deltas(hiddenLayers + 1) = gPrima(cell2mat(MembranePotentials(hiddenLayers + 1))) .* (ExpectedOutput - cell2mat(Outputs(hiddenLayers + 1)));
			
			for currentLayer = hiddenLayers : -1 : 1
				currentWeights = cell2mat(Weights(currentLayer + 1)); #get matrix
				currentWeights(:, [1]) = []; #remove bias weight
				currentWeights = currentWeights'
				Deltas(currentLayer) = gPrima(cell2mat(MembranePotentials(currentLayer))).* (currentWeights * cell2mat(Deltas(currentLayer + 1)))
			endfor
		
			#update weights 
			for currentLayer = 1 : hiddenLayers + 1
				currentLayer
				if(currentLayer == 1)
					DeltaWeights = learningFactor * cell2mat(Deltas(currentLayer)) * (CurrentPattern')
				else
					#here problems
					OutputWithBias = cell2mat(Outputs(currentLayer - 1))'; #get matrix and transpose
					OutputWithBias = [-1, OutputWithBias]; #Add bias 
					DeltaWeights = learningFactor * cell2mat(Deltas(currentLayer)) * OutputWithBias;
				endif
				Weights(currentLayer) = cell2mat(Weights(currentLayer)) + DeltaWeights;
			endfor
			
			epoch = epoch + 1;
			if(mod(epoch, inputSize / 2) == 0)
				currentError = acumError / 2;
				acumError = 0;
			endif  
		endif
	endfor
until (currentError < maxError)




