package ar.edu.itba.sia.Ohn0;

import java.util.List;

public class Problem implements ar.edu.itba.sia.gps.api.Problem {
    private State initialState;
    private List<ar.edu.itba.sia.gps.api.Rule> rules;

    public Problem(State initialState, List<ar.edu.itba.sia.gps.api.Rule> rules) {
        this.initialState = initialState;
        this.rules = rules;
    }

    @Override
    public ar.edu.itba.sia.gps.api.State getInitState() {
        return initialState;
    }

    @Override
    public boolean isGoal(ar.edu.itba.sia.gps.api.State state) {
        /// TODO: preguntar si está bien
        if (!state.getClass().equals(State.class)) {
            throw new IllegalArgumentException();
        }
        State currentState = (State) state;
        for (int i = 0; i < currentState.getSize(); i++) {
            for (int j = 0; j < currentState.getSize(); j++) {
                Cell cell = currentState.getCell(i, j);
                int value = cell.getValue();
                if (value > 0) {
                    if (!checkValidCell(i, j, currentState)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkValidCell(int row, int col, State state) {
        int value = state.getCell(row, col).getValue();
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = row, k = col; j < state.getSize() && j >= 0 && k < state.getSize() && k >= 0
                    && !state.getCell(j, k).isBlank() && !state.getCell(j, k).getColor().equals(Color.RED); j += directions[i][0], k += directions[i][1]) {
                if (j != row || k != col) {
                    value -= 1;
                }
            }
        }
        return value == 0;
    }

    @Override
    public List<ar.edu.itba.sia.gps.api.Rule> getRules() {
        return rules;
    }
}