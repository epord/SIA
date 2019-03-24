package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;

public class MissingRedsHeuristics implements Heuristic {
    @Override
    public Integer getValue(ar.edu.itba.sia.gps.api.State state) {
        int cumulated = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(ar.edu.itba.sia.gps.api.State.class)) {
            throw new IllegalArgumentException();
        }

        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                cumulated += currentBoard.getCell(i, j).getValue();
            }
        }
        return currentBoard.getSize() * currentBoard.getSize() - cumulated;
    }
}
