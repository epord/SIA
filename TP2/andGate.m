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

numberOfEpocs = 5;
learningFactor = 0.2;
Weights = 2 * rand(1, 3) - 1 # Matriz de 1x3 de números entre [-1, 1)

#TrainingInputs = [-1, 1, -1; -1, 1, 1];
#TrainingOutputs = [-1; 1];
trainingQuantity = 2;


for epoc = 1 : numberOfEpocs
	for row = 1 : trainingQuantity
		Input = [-1; sign(rand() - 0.5); sign(rand() - 0.5)] # Umbral más dos inputs de +1 o -1
		if(Input(2, 1) == 1 && Input(3, 1) == 1)
			expectedOutput = 1;
		else
			expectedOutput = -1;
		endif		
		output = sign(Weights * Input);
		
		if(output != expectedOutput)
			#should correct weights
			Weights(1, 1) = Weights(1, 1) + learningFactor * (1 - expectedOutput * output) * expectedOutput * Input(1, 1); 
			Weights(1, 2) = Weights(1, 2) + learningFactor * (1 - expectedOutput * output) * expectedOutput * Input(2, 1); 
			Weights(1, 3) = Weights(1, 3) + learningFactor * (1 - expectedOutput * output) * expectedOutput * Input(3, 1);
		endif
	endfor
endfor

Weights

#######################################################################
printf("Testing with resulting Weights:\n");
testQuantity = 1000;
failedTests = 0;

for row = 1 : testQuantity
	Input = [-1; sign(rand() - 0.5); sign(rand() - 0.5)];
	if(Input(2, 1) == 1 && Input(3, 1) == 1)
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
