package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) {

    }

    private static List<Rule> generateRules(Board board) {
        List<Rule> rules = new ArrayList<>();
        List<Arrow> arrows = board.getArrows();

        for(Arrow a: arrows) {
                rules.add(generateRule(board, a));
        }

        return rules;

    }

    private static Rule generateRule(Board board, final Arrow arrow) {
//        return new SimpleSquaresRule(String.format("%s @ (%d,%d)", arrow.getColor(), arrow.getDirection()), state -> {
//            Board currentBoard = (Board) state;
//            Map<Position, Piece> positionBoard = board.getBoard();
//            Position newPosition = arrow.moveArrow();
//            positionBoard.remove(arrow.getPosition());
//            Piece currentPiece;
//            Arrow currentArrow = arrow, newArrow;
//            Board newBoard;
//
//            boolean isArrow = true;
//            while(positionBoard.containsKey(newPosition) && isArrow) {
//                currentPiece = positionBoard.get(newPosition);
//                if(currentPiece.getClass().equals(arrow.getClass())) {
//                    newArrow = (Arrow)currentPiece;
//                    positionBoard.put(newPosition, currentArrow);
//                    newPosition = newArrow.moveArrow(currentArrow.getDirection());
//                }
//                else {
//                    isArrow = false;
//                }
//            }
//            positionBoard.put(newPosition, currentArrow);
//            newBoard = new Board(positionBoard, currentBoard.getArrows(), currentBoard.getObjetcives());//should generate newArrows instead
//            return Optional.of(newBoard);//why??
//        });
        return null;
    }
}
