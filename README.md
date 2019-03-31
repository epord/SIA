# SIA TP 1

## Boards
The boards are read from a file with the following format:
* N [int]: size of the board. This number is in a single line
* N lines with N space-separated characters from the following list:
    * **n**: a blue circle with an initial value *n*
    * **O**: a blue circle with no value 
    * **X**: a red circle with no value
    * **.**: a blank space
    
### Example
```
4
. . 2 .
X . . 4
. . 3 .
2 . . .
```

### Boards Generator
1. `cd boards/`
2. ```./boardGenerator.sh <size> <amount> <baseFilename>```

With:
* **size** *[int]*: the width and heigth of the boards to generate.
* **amount** *[int]*: the amount of boards to generate.
* **baseFilename** *[string]*: the base filename for each generated board. The boards will have the following names: *\<baseFilename>\<size>x\<size>_\<number>* (e.g.: board5x5_1, board5x5_2, board5x5_3, etc.)

## Building
1. `mvn package`
1. Output file is under `ohno/target/gps-1.0.jar`, relative to projet root

## Running
The program receives a *.properties* file with the following properties:
* **strategy [string: *BFS, DFS, IDDFS, GREEDY, ASTAR*]**:  which search strategy will be used.  
* **resolveMethod [int: *0-1*]**: 0 for fill blanks, 1 for heuristic repair.
* **heuristic [int: *1-7*]**: which heuristic will be used. The heuristics are:
    * 0 -> FillingBlanksHeuristic (only with fill blanks resolve method)
    * 1 -> ConflictingNumbersHeuristic (only heuristic reparation)
    * 2 -> FillBlanksNonTrivialAdmisibleHeuristic (only with fill blanks resolve method)
    * 3 -> HeuristicReparationAdmisibleHeuristic (only heuristic reparation)
    * 4 -> AverageRedsHeuristic (only with fill blanks resolve method)
    * 5 -> MissingRedsHeuristics (fill blanks and heuristic reparation resolve methods)
    * 6 -> MissingVisibleBlueHeuristics (fill blanks and heuristic reparation resolve methods)
    * 7 -> AddAllHeuristics (only with fill blanks resolve method)
    
    &nbsp;&nbsp;&nbsp;&nbsp;The explanation of each heuristic can be found in XXXXinforme.docXXXX 
    
* **fillingMethod [string: *blue, red, random*]**: (only for heuristic repair) This is how to board will be filled: only with blue/red circles or random color. 
* **board [string]**: path of the board to solve (relative to CWD or absolute (preferred)).

Run command:

`java -jar ohno/target/gps-1.0.jar <path_to_properties (optional)>`

By default, the program will use the file `settings.properties` in the CWD, but you can optionally provide the path to your custom .properties file (relative to CWD or absolute (preferred)).


###Running example
```properties
strategy=ASTAR
heuristic=1
resolveMethod=1
fillingMethod=blue
board=/Users/user/AI_Engine/boards/board8x8_1
```

`java -jar ohno/target/gps-1.0.jar /Users/user/AI_Engine/settings.properties`

