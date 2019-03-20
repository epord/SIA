package ar.edu.itba.sia.Ohn0;

import com.sun.javaws.exceptions.InvalidArgumentException;

public class State implements ar.edu.itba.sia.gps.api.State {

    private Cell[][] cells;
    private Integer size;

    public State(Cell[][] cells, Integer size) {
        this.cells = cells;
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
                builder.append(cells[i][j].toString());
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && o.getClass() != this.getClass()) return false;
        State obj = (State) o;
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
