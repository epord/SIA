package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.Ohn0.Color;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

public class MissingVisibleBlueHeuristics implements Heuristic {
    @Override
    public Integer getValue(State state) {
        int cumulated = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }

        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                cumulated += currentBoard.missingVisibleBlues(i, j);
            }
        }
        return cumulated;
    }
}
