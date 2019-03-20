package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.State;
import ar.edu.itba.sia.gps.api.Heuristic;

public class MissingRedsHeuristics implements Heuristic {
    @Override
    public Integer getValue(ar.edu.itba.sia.gps.api.State state) {
        int cumulated = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(ar.edu.itba.sia.gps.api.State.class)) {
            throw new IllegalArgumentException();
        }

        State currentState = (State) state;
        for (int i = 0; i < currentState.getSize(); i++) {
            for (int j = 0; j < currentState.getSize(); j++) {
                cumulated += currentState.getCell(i, j).getValue();
            }
        }
        return currentState.getSize() * currentState.getSize() - cumulated;
    }
}
