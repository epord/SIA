#!/bin/bash
re='^[0-9]+$'
if ! [[ $1 =~ $re ]] || [ $1 -lt 4 ]
    then
        echo "error: $1 is not a valid board size, it must be a positive integer >= 4." >&2; exit 1
fi
if ! [[ $2 =~ $re ]] || [ $2 -lt 1 ]
    then
        echo "error: $2 is not a valid number, it must be a positive integer >= 1." >&2; exit 1
fi

echo "Generating boards..."
for i in $(seq 1 $2)
    do
        node ./boardGenerator.js $1 "$3$1X$1_$i";
        echo "Board nº$i generated"
done
echo "Boards generated successfully!"