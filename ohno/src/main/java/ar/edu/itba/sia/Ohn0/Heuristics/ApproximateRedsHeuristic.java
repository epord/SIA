package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;


/**
 *  Only for filling blanks
 *
 *  Description: Calculates aproximately how many blues are missing
 *  for each number on the board and add them together, count how many
 *  blanks spaces are currently on the board and substracts to it the
 *  previous sum giving this an average quantity of reds.
 *
 *  The problem with this approach is that missing blues is not accurate,
 *  because adding one blue piece can count as more than one missing blue
 *  if it is reached by more than one number.
 */
public class ApproximateRedsHeuristic implements Heuristic {
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
    public String getName() {
        return "4(Average Red Heuristic)";
    }

    public String toString() {
        return getName();
    }
}
