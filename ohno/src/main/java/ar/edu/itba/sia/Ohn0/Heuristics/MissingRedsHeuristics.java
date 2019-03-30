package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.Ohn0.Color;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;
/*
**  Works with heuristic reparation and filling the blanks
**
**  Description: Sums all blue with numbers and substracts that
**  value to the board size, also substracts fixed counts and red
**  dots, giving this a minimum of non visible pieces that the player
**  needs to complete.
**
**  Is a really bad heuristic because it generates a lots of ties and also
**  when boards are big board size - cumulated gives less than zero, so
**  heuristic value on that case would be zero always even in non goal states.
 */
public class MissingRedsHeuristics implements Heuristic {
    @Override
    public Integer getValue(State state) {
        int cumulated = 0;
        int fixedCount = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }

        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                int value = currentBoard.getCell(i, j).getValue();
                cumulated += value;
                if (value > 0 || currentBoard.getCell(i, j).getColor() == Color.RED) fixedCount++;
            }
        }
        return Math.max(currentBoard.getSize() * currentBoard.getSize() - cumulated - fixedCount, 0);
    }
}
