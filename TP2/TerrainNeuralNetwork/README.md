# Terrain Neural Network

This is an Octave algoritm that trains a neural network to learn a 3D terrain. Given an X and a Y as inputs, the neural network emits the Z as ouput.
Unless configured otherwise, as the network gets trained the program will plot the error, until a desirable error or a maximum number of epochs is reached, whichever comes first. The program will then also plot the original terrain, superimposed with the terrain that the neural net learned.

# Usage

The main file is `multiLayerPerceptron.m`.  Either open that file with Octave GUI, or for command line run
```bash
octave multiLayerPerceptron.m
```

**NOTE:** The configuration file must exist. See the configuration section.

# Configuration

## Neural Network

The neural network itself has many parameters that must be configured. Copy the `architecture.conf.example` file as `architecture.conf` and tweak parameters as needed.  The example file comes with some sensible but not necessarily optimal parameters.
There is also an example of the optimal neural network configuration in the file `optimalArchitecture.conf`

## Command-Line Arguments (only for CLI, not GUI)
It is posible to provide command-line arguments to the execution:
* `--silent`: won't print the current error at each epoch
* `--no-plot`: won't show the error plot at each epoch
* `--auto-exit`: won't ask for a key to terminate the program

# Debugging
## GUI (all platforms, doesn't always work)
1. Open desired files/functions (most functions are extracted to a file)
1. Set breakpoints
1. Open the main file `multiLayerPerceptron.m`
1. Run the file

Octave should (but apparently doesn't always) stop at breakpoints. Step functionality (step, step in, etc.) should work too, accross files.

## CLI
1. Call the `keyboard();` function where you would like the code to stop
1. Run the project (see usage section)
1. See [the documentation](https://octave.org/doc/v4.4.1/Debugging.html) as to how to control the debugger

### With Interrupts (only works on Linux)
1. Run the project (see usage section)
1. Interrupt the program with `CTRL+C`. Program will enter debug mode.
1. See [the documentation](https://octave.org/doc/v4.4.1/Debugging.html) as to how to control the debugger
