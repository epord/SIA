package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.gps.api.Heuristic;

public class MissingRedsHeuristics implements Heuristic {
    @Override
    public Integer getValue(ar.edu.itba.sia.gps.api.State state) {
        int cumulated = 0;
        int fixedBlueCount = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }

        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                int value = currentBoard.getCell(i, j).getValue();
                cumulated += value;
                if (value > 0) fixedBlueCount++;
            }
        }
        return Math.max(currentBoard.getSize() * currentBoard.getSize() - cumulated - fixedBlueCount, 0);
    }
}
