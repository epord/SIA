package ar.edu.itba.sia;

public class Arrow extends Piece{
    int color;
    Direction direction;
    Position objective;

    public Arrow(int color, Direction direction, int objrow, int objcol, int row, int col) {
        super(row, col);
        this.color = color;
        this.direction = direction;
        objective = new Position(objrow, objcol);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getColor() {
        return color;
    }

    public void setDirection(int newRow, int newCol) {
         getPosition().setPosition(newRow, newCol);
    }

    public Position moveArrow() {
        Position newPosition = null;
        switch(direction) {
            case UP:
                newPosition = new Position(getPosition().getCol() - 1, getPosition().getCol());
                break;
            case RIGHT:
                newPosition = new Position(getPosition().getCol() , getPosition().getCol() + 1);
                break;
            case DOWN:
                newPosition = new Position(getPosition().getCol() + 1, getPosition().getCol());
                break;
            case LEFT:
                newPosition = new Position(getPosition().getCol(), getPosition().getCol() - 1);
                break;
        }

        return newPosition;
    }

    public Position moveArrow(Direction pushedDirection) {
        Position newPosition = null;
        switch(pushedDirection) {
            case UP:
                newPosition = new Position(getPosition().getCol() - 1, getPosition().getCol());
                break;
            case RIGHT:
                newPosition = new Position(getPosition().getCol() , getPosition().getCol() + 1);
                break;
            case DOWN:
                newPosition = new Position(getPosition().getCol() + 1, getPosition().getCol());
                break;
            case LEFT:
                newPosition = new Position(getPosition().getCol(), getPosition().getCol() - 1);
                break;
        }

        return newPosition;
    }

    public int getDistanceToObjective() {
        return (objective.getRow() - getPosition().getRow()) + (objective.getCol() - getPosition().getCol());
    }
}
