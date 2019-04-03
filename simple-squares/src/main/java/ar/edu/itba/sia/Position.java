package ar.edu.itba.sia;

public class Position {
    int row;
    int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public void setPosition(int newRow, int newCol) {
        row = newRow;
        col = newCol;
    }
}
