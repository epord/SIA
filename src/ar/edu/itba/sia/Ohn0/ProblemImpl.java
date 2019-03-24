package ar.edu.itba.sia.Ohn0;

import java.util.List;

public class ProblemImpl implements ar.edu.itba.sia.gps.api.Problem {
    private Board initialBoard;
    private List<ar.edu.itba.sia.gps.api.Rule> rules;

    public ProblemImpl(Board initialBoard, List<ar.edu.itba.sia.gps.api.Rule> rules) {
        this.initialBoard = initialBoard;
        this.rules = rules;
    }

    @Override
    public ar.edu.itba.sia.gps.api.State getInitState() {
        return initialBoard;
    }

    @Override
    public boolean isGoal(ar.edu.itba.sia.gps.api.State state) {
        /// TODO: preguntar si está bien
        if (!state.getClass().equals(Board.class)) {
            throw new IllegalArgumentException();
        }
        Board currentBoard = (Board) state;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                Cell cell = currentBoard.getCell(i, j);
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
    public List<ar.edu.itba.sia.gps.api.Rule> getRules() {
        return rules;
    }
}