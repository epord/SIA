package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.State;
import ar.edu.itba.sia.gps.api.Heuristic;

public class FillingBlanksHeuristic implements Heuristic {

    @Override
    public Integer getValue(ar.edu.itba.sia.gps.api.State state) {
        int blankCount = 0;

        /// TODO: preguntar si está bien
        if (!state.getClass().equals(State.class)) {
            throw new IllegalArgumentException();
        }

        State currentState = (State) state;
        for (int i = 0; i < currentState.getSize(); i++) {
            for (int j = 0; j < currentState.getSize(); j++) {
                if (currentState.getCell(i, j).isBlank()) {
                    blankCount++;
                }
            }
        }
        return blankCount;
    }
}
