package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;


/**
 *  Only for filling blanks
 */
public class AverageRedsHeuristic implements Heuristic {
    @Override
    public Integer getValue(State state) {

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }

        Board currentBoard = (Board) state;
        int cumulatedMissingBlues = 0;
        int blankCount = 0;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                if (currentBoard.getCell(i, j).getValue() > 0) {
                    cumulatedMissingBlues += currentBoard.missingBlues(i, j);
                }
                else if (currentBoard.getCell(i, j).isBlank()) {
                    blankCount++;
                }
            }
        }
        return Math.max(blankCount - cumulatedMissingBlues, 0);
    }
}
