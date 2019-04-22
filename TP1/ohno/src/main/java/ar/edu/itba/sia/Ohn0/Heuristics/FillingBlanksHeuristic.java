package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;
/*
* Only for filling the blanks
*
* Description: It returns the number of blank spaces currently on the board.
*
* It is a trivial admisible heuristic.
*
 */
public class FillingBlanksHeuristic implements Heuristic {

    @Override
    public Integer getValue(State state) {
        int blankCount = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }

        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                if (currentBoard.getCell(i, j).isBlank()) {
                    blankCount++;
                }
            }
        }
        return blankCount;
    }

    public String getName() {
        return "0(Filling Blanks Heuristic)";
    }

    public String toString() {
        return getName();
    }
}
