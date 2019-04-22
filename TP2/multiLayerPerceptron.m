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

#start of code


hiddenLayers 	= 1;
hiddenUnits  	= 2;
outputUnits  	= 1;
inputUnits   	= 2;
currentError 	= 1;
maxError		= 0.005;

#Generate weights TODO: more generic with function jejeje
Weights 		= cell(hiddenLayers + 1, 1);
Weights(1)		= (rand(hiddenUnits, inputUnits + 1) - 0.5);
Weights(2)		= (rand(outputUnits, hiddenUnits + 1) - 0.5);

Outputs 		= cell(hiddenLayers + 1, 1);
MembranePotentials = cell(hiddenLayers + 1, 1);
inputSize		= 2 ** inputUnits;
Patterns 		= getPatterns(inputUnits);
# Output 			= 0;

do 	
	inputOrder = suffle(1 : inputSize, inputSize);
	
	for index = 1 : inputSize
		CurrentPattern  = Patterns(:, inputOrder(index));
		Input 			= CurrentPattern; 
		
		for currentLayer = 1 : hiddenLayers + 1
			Output = Weights(currentLayer) * Input;
			MembranePotentials(currentLayer) = Output;
			Output = g(Output);
			Outputs(currentLayer) = Output;
			
			if(currentLayer < hiddenLayers)
				Input  = [-1; Output];
			endif
		endfor 
	endfor

	#fix weights and backpropagation
	ExpectedOutput = ExpectedOutputs(inputOrder(index)); #TODO
	for index = inputSize : -1 : 1
		Deltas = gPrima(MembranePotentials(index)).*(ExpectedOutput - Outputs(index));
	endfor
until (currentError < maxError)




