# Genetic Algorithm
This project is a genetic algorithm that finds the equipment that maximizes the stats of a warrior, from a database of
1 million each of helmets, platebodies, gloves, weapons and boots.

# Building
1. `mvn package`
1. Done! Runnable JAR is `target/genetics-1.0-SNAPSHOT.jar`.

# Running
1. Make a copy of `example.properties` and name it `settings.properties`
1. Change settings as desired
1. Run `java -jar path-to-jar.jar [path-to-settings]`
    - If path to settings is not specified, program will look for `settings.properties` in the same directory
    
## Memory
Depending on the chosen settings, the program can easily run out of memory. To run the JAR with more allowed memory, run
it like so:

`java -Xmx1G -jar path-to-jar.jar [path-to-settings]` to run with 1GB.

See more details [here](https://stackoverflow.com/a/14763095/2333689).

# Configuration
See the comments within `example.properties` which extensively document what each option does. **NOTE:** Not all settings
are read for a particular run (eg. `*.selection.second.*` properties are only read when Boltzmann or
Ranking is chosen as second method)

# Output
When the program reaches to an end, the console will show the best warrior's fitness found and will output two files:

- `out.m` Octave file that generates the graphs showing the evolution of the population
- `frontend/data.p5` (if `frontend` subdirectory exists, otherwise outputs to same directory) Animation file used in `frontend/index.html`
to visualize the evolution of stats and diversity in the population.

# Visualization
1. Run the program (see [Running](#running))
1. Open `frontend/index.html` in your browser
1. Animation should start automatically
