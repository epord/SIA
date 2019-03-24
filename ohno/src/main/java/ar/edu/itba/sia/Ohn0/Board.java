package ar.edu.itba.sia.Ohn0;


import java.util.Random;

public class Board implements ar.edu.itba.sia.gps.api.State {

    private Cell[][] cells;
    private Integer size;

    public Board(Cell[][] cells, Integer size) {
        this.cells = new Cell[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.cells[i][j] = cells[i][j];
            }
        }
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size){
            throw new IllegalArgumentException();
        }
        return cells[row][col];
    }

    @Override
    public String getRepresentation() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                builder.append(cells[i][j].toString() + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private int adjacentReds(int row, int col) {
        int redCount = 0;
        if (row - 1 < 0 || row - 1 >= size || getCell(row - 1, col).getColor() == Color.RED) redCount++;
        if (row + 1 < 0 || row + 1 >= size || getCell(row + 1, col).getColor() == Color.RED) redCount++;
        if (col - 1 < 0 || col - 1 >= size || getCell(row, col - 1).getColor() == Color.RED) redCount++;
        if (col + 1 < 0 || col + 1 >= size || getCell(row, col + 1).getColor() == Color.RED) redCount++;
        return redCount;
    }

    public Board switchColor(int row, int col, Color newColor) {
        if(row < 0 || row >= size || col < 0 || col >= size || cells[row][col].getFixed() ||
                newColor.equals(cells[row][col].getColor())) {
            return null;
        }
        if(newColor == Color.BLUE) {
            int redCounter = adjacentReds(row, col);
            if (redCounter == 4) return null;
        }
        Board newBoard = new Board(cells, size);
        newBoard.cells[row][col] = new Cell(false, 0, newColor);
        return newBoard;
    }

    public boolean isChangeValid(int row, int col) {
        boolean value;
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = row + directions[i][0], k = col + directions[i][1]; j < getSize() && j >= 0 && k < getSize() && k >= 0
                    && !getCell(j, k).isBlank() && !getCell(j, k).getColor().equals(Color.RED); j += directions[i][0], k += directions[i][1]) {
                if (getCell(j, k).getValue() > 0) {
                    value = isNumberCorrect(j,k);
                    if (!value) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isNumberCorrect(int row, int col) {
        int value = getCell(row, col).getValue();
        int blanks = 0;
        int directions[][] = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = row + directions[i][0], k = col + directions[i][1]; j < getSize() && j >= 0 && k < getSize() && k >= 0
                     && !getCell(j, k).getColor().equals(Color.RED); j += directions[i][0], k += directions[i][1]) {
                if(getCell(j, k).isBlank()) {
                    blanks++;
                }
                else {
                    value -= 1;
                }
            }
        }
        return value == 0 || (value > 0 && blanks > 0 && value <= blanks);
    }

    public Board fillRandomly() {
        Board newBoard = new Board(cells, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (newBoard.getCell(i, j).isBlank()) {
                    Random r = new Random();
                    Color randomColor = r.nextBoolean() ? Color.RED : Color.BLUE;
                    newBoard.cells[i][j] = new Cell(false, 0, randomColor);
                }
            }
        }
        return newBoard;
    }

    public Board fillBlue() {
        Board newBoard = new Board(cells, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (newBoard.getCell(i, j).isBlank()) {
                    Random r = new Random();
                    newBoard.cells[i][j] = new Cell(false, 0, Color.BLUE);
                }
            }
        }
        return newBoard;
    }

    @Override
    public String toString() {
        return  getRepresentation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && o.getClass() != this.getClass()) return false;
        Board obj = (Board) o;
        if (obj.size != this.size) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!obj.cells[i][j].equals(cells[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getRepresentation().hashCode();
    }
}
