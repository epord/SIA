function [inputs, outputs] = readDataFile(filename)
  data = dlmread(filename)(2:end, :); # Skip first row since it's headers
  inputs = data(:, 1:2);
  outputs = data(:, 3);
endfunction
