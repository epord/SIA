package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

/*
 *   Works for heuristic reparation, can be implemented on fill in the blanks making
 *   an implementation of isNumberCorrect suitable for that purpose.
 *
 *  Description: Calculates how many conflicts are currently on the board
 *  and it returns thar.
 *
 *  The problem with this heuristic is that one move may solve one or more
 *  conflicts so this heuristic is not admisible, even though it seems to be
 *  a good heuristic
 */
public class ConflictingNumbersHeuristic implements Heuristic {
    @Override
    public Integer getValue(State state) {
        int conflictCount = 0;
        Board currentBoard = (Board) state;
        for(int i = 0; i < currentBoard.getSize(); i++) {
            for(int j = 0; j < currentBoard.getSize(); j++) {
                if(currentBoard.getCell(i, j).getValue() > 0) {
                    if(!currentBoard.isNumberCorrectHeuristicReparation(i,j)) {
                        conflictCount++;
                    }
                }
            }
        }

        return conflictCount;
    }

    public String getName() {
        return "1(Conflicting Numbers Heuristic)";
    }

    public String toString() {
        return getName();
    }
}
