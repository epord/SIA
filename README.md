# SIA TP 1

## Building
1. `mvn package`
1. Output file is under `ohno/target/gps-1.0.jar`, relative to projet root

## Running
1. `java -jar ohno/target/gps-1.0.jar`

## Boards Generator
1. `cd boards/`
2. ```./boardGenerator.sh <size> <amount> <baseFilename>```

With:
* **size** *[int]*: the width and heigth of the boards to generate.
* **amount** *[int]*: the amount of boards to generate.
* **baseFilename** *[string]*: the base filename for each generated board. The boards will have the following names: *\<baseFilename>\<size>x\<size>_\<number>* (e.g.: board5X5_1, board5x5_2, board5x5_3, etc.)