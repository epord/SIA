package ar.edu.itba.sia;

public class Piece {
    private Position position;

    public Piece(int row, int col) {
        position = new Position(row, col);
    }

    public Position getPosition() {
        return position;
    }
}
