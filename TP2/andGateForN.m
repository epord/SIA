#	AND GATE
#   | x1 | x2 | output |
#    ------------------
#   | -1 | -1 |  -1    |
#   | -1 |  1 |  -1    |
#   |  1 | -1 |  -1    |
#   |  1 |  1 |   1    |		
#
# bias wieght always set to -1
# 

1; #to prevent it from being a function file

function newVector = shuffle(vector, vectorSize)
	newVector = vector;
	for index = 1 : vectorSize
			newIndex = floor(rand() * vectorSize + 1);
			aux = newVector(newIndex);
			newVector(newIndex) = newVector(index);
			newVector(index) = aux;
		endfor
endfunction

function pattern = getPatterns(N)
	if(N == 2)
		pattern = [-1, -1,  -1, -1;
			  		1,  1,  -1, -1;
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


function total = getTotalSum(vector, dim) 
	total = 0;
	for i = 1 : dim
		total = total + vector(i);
	endfor
endfunction

###########################################################################################################################################################
#----------------------------------------->    start of code    <-----------------------------------------#################################################

N 				 = 5
numberOfEpochs   = 10;
learningFactor   = 0.4;
inputSize        = 2 ** N;
inputOrder	     = 1 : inputSize;
trainingQuantity = inputSize;
Patterns 		 = getPatterns(N);
Weights          = 2 * rand(1, N + 1) - 1
sum 			 = 0;

for epoch = 1 : numberOfEpochs
	inputOrder = shuffle(inputOrder, inputSize);
	for number = 1 : trainingQuantity
		Input = Patterns(:, number);
		sum = getTotalSum(Patterns([2 : N + 1], number), N);
		
		if(sum == N)
			expectedOutput = 1;
		else
			expectedOutput = -1;
		endif	
		
		output = sign(Weights * Input);
		
		if(output != expectedOutput)
			#should correct weights
			for i = 1 : N + 1
				Weights(1, i) = Weights(1, i) + learningFactor * (1 - expectedOutput * output) * expectedOutput * Input(i, 1);
			endfor
		endif
	endfor
endfor

Weights

#######################################################################
printf("Testing with resulting Weights:\n");
testQuantity = 1000;
failedTests = 0;
Input = zeros (N + 1, 1);
Input(1, 1) = -1;

for number = 1 : testQuantity
	sum = 0;
	for i = 2 : N + 1
		Input(i, 1) = sign(rand() - 0.5);
		sum = sum + Input(i, 1);
	endfor

	if(sum == N)
		expectedOutput = 1;
	else
		expectedOutput = -1;
	endif		

	output = sign(Weights * Input);
	
	if(output != expectedOutput)
		failedTests = failedTests + 1;
	endif
endfor

testQuantity
failedTests