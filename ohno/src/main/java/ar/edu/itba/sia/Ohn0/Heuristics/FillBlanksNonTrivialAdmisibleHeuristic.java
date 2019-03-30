package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.Ohn0.Color;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

public class FillBlanksNonTrivialAdmisibleHeuristic implements Heuristic {

    Set<Position> conflictingNumbers;
    FillBlanksNonTrivialAdmisibleHeuristic() {
        conflictingNumbers = new HashSet<>();
    }

    @Override
    public Integer getValue(State state) {
        conflictingNumbers.clear();
        int conflictCount = getConflictingNumbers(state);
        int maxConflictQuantity = 0;
        int nonVisiblePieces = 0, aux;
        Board currentBoard = (Board) state;

        for(int i = 0; i < currentBoard.getSize(); i++) {
            for(int j = 0; j < currentBoard.getSize(); j++) {
                if(currentBoard.getCell(i, j).isBlank()) {
                    aux = getConflictingNeighbours(currentBoard, i, j);
                    if(aux < 0) {
                        nonVisiblePieces++;
                    }
                    else if(aux > 0 && aux > maxConflictQuantity) {
                        maxConflictQuantity = aux;
                    }
                }
            }
        }
        if(maxConflictQuantity > 0) {
            return Math.max(conflictCount/maxConflictQuantity, 1) + nonVisiblePieces;
        }

        return maxConflictQuantity + nonVisiblePieces;
    }

    private int getConflictingNeighbours(Board board, int row, int col) {
        int conflictingNeighboursCount = 0;
        int numberSeenCount = 0;
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = row + directions[i][0], k = col + directions[i][1]; j < board.getSize() && j >= 0 && k < board.getSize() && k >= 0
                    && board.getCell(j,k).getColor().equals(Color.BLUE); j += directions[i][0], k += directions[i][1]) {
                if(board.getCell(i, j).getValue() > 0) {
                    numberSeenCount++;
                    if(conflictingNumbers.contains(new Position(i, j))) {
                        conflictingNeighboursCount++;
                    }
                }
            }
        }

        return numberSeenCount == 0 ? -1 : conflictingNeighboursCount;
    }

    private int getConflictingNumbers(State state) {
        int conflictCount = 0;
        Board currentBoard = (Board) state;
        for(int i = 0; i < currentBoard.getSize(); i++) {
            for(int j = 0; j < currentBoard.getSize(); j++) {
                if(currentBoard.getCell(i, j).getValue() > 0) {
                    if(hasConflicts(currentBoard, i, j)) {
                        conflictCount++;
                        conflictingNumbers.add(new Position(i,j));
                    }
                }
            }
        }

        return conflictCount;
    }

    private boolean hasConflicts(Board board, int row, int col) {
        int value = board.getCell(row, col).getValue();
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = row + directions[i][0], k = col + directions[i][1]; j < board.getSize() && j >= 0 && k < board.getSize() && k >= 0
                    && board.getCell(j,k).getColor().equals(Color.BLUE); j += directions[i][0], k += directions[i][1]) {
                value -= 1;
            }
        }
        return value != 0;
    }

    private static class Position {
        int row;
        int col;
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o != null && o.getClass() != this.getClass()) return false;
            Position obj = (Position) o;
            return (obj.row == this.row) && (obj.col == this.col);
        }

        @Override
        public int hashCode() {
            //Objects.hashCode(cells);
            HashCodeBuilder hashBuilder = new HashCodeBuilder(17, 37);
            hashBuilder.append(row).append(col);
            return hashBuilder.toHashCode();
        }
    }

}
