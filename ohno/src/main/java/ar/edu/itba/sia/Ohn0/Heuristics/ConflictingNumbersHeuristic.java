package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

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
}
