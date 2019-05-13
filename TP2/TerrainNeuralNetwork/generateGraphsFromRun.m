# This script was used to reconstruct the graphs for the 2-hidden-layer runs since the terrain graphs didn't get generated, although the .state files did.

clear all;
load "/home/puma/Desktop/2-capas/seed1bis/runs/2  10  10   1.state";

global hiddenLayers = size(UnitsQuantity)(2) - 2;
global Weights;
global MembranePotentials = cell(hiddenLayers + 1, 1);
global Outputs = cell(hiddenLayers + 1, 1);
global beta = 1;


function [result] = g(input)
	global beta;
	result = 1.0 ./ (1.0 + exp(-2 * beta * input));
endfunction

function [result] = gPrima(output)
	global beta;
	result = 2 * beta * output .* (1 - output); #exp derivative
endfunction

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
plot(Errors)
xlabel("Epochs")
xlim([0 350])
set(gca,'XTick',0:50:350) # Set X ticks every 50 epochs
ylabel("Mean Squared Error")
ylim([0 0.2])
title(cstrcat("Mean Squared Error - Configuration ", num2str(UnitsQuantity)))
filename = strcat("plots/errors-", num2str(UnitsQuantity));
filename = strrep(filename, " ", "_");
print(filename, "-dsvg")

# Save generated terrain positions plot
figure(2);
plot3(Positions(2,:), Positions(3,:), plotOutputs, ".", "color", "blue")
title(cstrcat("Generated terrain positions - Configuration ", num2str(UnitsQuantity)))
axis([-3 3 -3 3 -1 1]);
xlabel("X")
ylabel("Y")
zlabel("Z (network output)")
filename = strcat("plots/generatedPoints-", num2str(UnitsQuantity));
filename = strrep(filename, " ", "_");
% print(filename, "-dsvg")

hold on

# Save generated AND provided terrain positions plot
plot3(ProvidedPatterns(2,:), ProvidedPatterns(3,:), ProvidedOutputs, "*", "color", "red")
legend("Generated", "Provided");
axis([-3 3 -3 3 -1 1]);
title(cstrcat("Both terrain positions - Configuration ", num2str(UnitsQuantity)))
xlabel("X")
ylabel("Y")
zlabel("Z")
filename = strcat("plots/both-", num2str(UnitsQuantity));
filename = strrep(filename, " ", "_");
print(filename, "-dsvg")
