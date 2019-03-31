package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.Ohn0.Color;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;
/*
*  Only for heuristic reparation
*
*  Description: First it counts all numbers on the board that doesn't
*  satisfy its restriction(conflicting numbers), then for each non-fixed
*  dot it calculate how many of these conflicting numbers are reachable
*  by a blue path from the dot on the same direction, it keeps
*  the greatest number of neighbours and returns the number of conflicts
*  divided by the maximum of conflicting neighbours(in case this operation
*  is greater than zero) or one
*
* Should sum also one for each isla de un azul rodeado de rojo TODO
*  This heuristic seems to be admisible

*/
public class HeuristicReparationAdmisibleHeuristic implements Heuristic {
    Set<Position> conflictingNumbers;

    public HeuristicReparationAdmisibleHeuristic() {
        conflictingNumbers = new HashSet<>();
    }

    @Override
    public Integer getValue(State state) {
        conflictingNumbers.clear();
        int conflictCount = getConflictingNumbers(state);
        int islandCount = 0;
        int maxConflictQuantity = 0;
        int aux;
        Board currentBoard = (Board) state;

        for(int i = 0; i < currentBoard.getSize(); i++) {
            for(int j = 0; j < currentBoard.getSize(); j++) {
                if(!currentBoard.getCell(i, j).getFixed()) {
                    aux = getConflictingNeighbours(currentBoard, i, j);
                    if(aux > 0 && aux > maxConflictQuantity) {
                        maxConflictQuantity = aux;
                    }
                    else if(aux < 0) {
                        islandCount++;
                    }
                }
            }
        }


        return maxConflictQuantity > 0 ? conflictCount-maxConflictQuantity + 1 + islandCount : conflictCount - maxConflictQuantity + islandCount;
    }

    private int getConflictingNeighbours(Board board, int row, int col) {
        int conflictingNeighboursCount = 0;
        int bluesSeen = 0;
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        if(board.getCell(row, col).getColor() == Color.RED) {
            bluesSeen = 1;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = row + directions[i][0], k = col + directions[i][1]; j < board.getSize() && j >= 0 && k < board.getSize() && k >= 0
                    && board.getCell(j,k).getColor().equals(Color.BLUE); j += directions[i][0], k += directions[i][1]) {
                if(board.getCell(j,k).getColor() == Color.BLUE) {
                    bluesSeen++;
                }
                if(board.getCell(i, j).getValue() > 0) {
                    if(conflictingNumbers.contains(new Position(i, j))) {
                        conflictingNeighboursCount++;
                    }
                }
            }
        }

        if(bluesSeen == 0) {
            return -1;
        }

        return conflictingNeighboursCount;
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

    public String getName() {
        return "3(Heuristic Reparation Admisible Heuristic)";
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
            HashCodeBuilder hashBuilder = new HashCodeBuilder(17, 37);
            hashBuilder.append(row).append(col);
            return hashBuilder.toHashCode();
        }
    }
}
