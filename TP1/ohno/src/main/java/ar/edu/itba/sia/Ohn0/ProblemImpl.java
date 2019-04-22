package ar.edu.itba.sia.Ohn0;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.List;

public class ProblemImpl implements Problem {
    private Board initialBoard;
    private List<Rule> rules;

    public ProblemImpl(Board initialBoard, List<Rule> rules) {
        this.initialBoard = initialBoard;
        this.rules = rules;
    }

    @Override
    public State getInitState() {
        return initialBoard;
    }

    @Override
    public boolean isGoal(State state) {
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }
        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                Cell cell = currentBoard.getCell(i, j);
                if(currentBoard.getCell(i, j).isBlank()) {
                    return false;
                }
                int value = cell.getValue();
                if (value > 0) {
                    if (!checkValidCell(i, j, currentBoard)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkValidCell(int row, int col, Board board) {
        int value = board.getCell(row, col).getValue();
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = row, k = col; j < board.getSize() && j >= 0 && k < board.getSize() && k >= 0
                    && !board.getCell(j, k).getColor().equals(Color.RED); j += directions[i][0], k += directions[i][1]) {
                if(board.getCell(j, k).isBlank() ) {
                    return false;
                }
                else if (j != row || k != col) {
                    value -= 1;
                }
            }
        }
        return value == 0;
    }

    @Override
    public List<Rule> getRules() {
        return rules;
    }
}