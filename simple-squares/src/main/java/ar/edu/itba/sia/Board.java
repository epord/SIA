package ar.edu.itba.sia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Position, Piece> board;
    private List<Arrow> arrows;
    private List<Objective> objetcives;

    public Board(List<Arrow> arrows, List<Objective> objectives) {
        board = new HashMap<>();
        this.arrows = arrows;
        this.objetcives = objectives;

        for(Arrow a : arrows) {
            board.put(a.getPosition(), a);
        }

        for(Objective o : objectives) {
            board.put(o.getPosition(), o);
        }
    }
    public Board(Map<Position, Piece> board, List<Arrow> arrows, List<Objective> objectives) {
        this.board = board;
        this.arrows = arrows;
        this.objetcives = objectives;
    }
    public List<Arrow> getArrows() {
        return arrows;
    }

    public List<Objective> getObjetcives() {
        return objetcives;
    }

    public Map<Position, Piece> getBoard() {
        return board;
    }
}
