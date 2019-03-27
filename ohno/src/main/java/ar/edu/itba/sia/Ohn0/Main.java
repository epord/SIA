package ar.edu.itba.sia.Ohn0;

import ar.edu.itba.sia.Ohn0.Heuristics.*;
import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.GPSEngineFactory;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Rule;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main {

    public static void main(String[] args) throws IOException {
        FileManager fm = new FileManager();
        Board board = fm.readStateFromFile(Paths.get("board8X8_2"));
//        Heuristic heuristic = new MissingVisibleBlueHeuristics();
        Heuristic heuristic = new ConflictingNumbersHeuristic();
//        runFillBlanks(board, SearchStrategy.GREEDY, heuristic);
        GPSEngine engine;
//        engine = getFillBlanksEngine(board, SearchStrategy.IDDFS, heuristic);
        engine = getHeuristicRepairEngine(board, SearchStrategy.ASTAR, heuristic);

        long startTime = System.currentTimeMillis();
        engine.findSolution();
        long endTime = System.currentTimeMillis();

        if (engine.isFailed()) {
            System.out.println("FAILED");
        } else {
            engine.printSolution();
        }
        System.out.println("\n" + (endTime - startTime) + " ms");
    }

    private static GPSEngine getFillBlanksEngine(Board board, SearchStrategy strategy, Heuristic heuristic) {
        List<Rule> problemRules = generateRulesFilling(board.getSize());
        ProblemImpl OhN0 = new ProblemImpl(board, problemRules);
        return GPSEngineFactory.getEngineForStrategy(OhN0, strategy, heuristic);
    }

    private static GPSEngine getHeuristicRepairEngine(Board board, SearchStrategy strategy, Heuristic heuristic) {
        board = board.fillBlue();
        List<Rule> reparationRules = generateRulesReparation(board.getSize(), false);
        ProblemImpl OhN0 = new ProblemImpl(board, reparationRules);
        return GPSEngineFactory.getEngineForStrategy(OhN0, strategy, heuristic);

    }

    private static List<Rule> generateRulesReparation(int size, Boolean onlyBlue) {
        int i, j;
        List<Rule> rules = new ArrayList<>();

        for(i = 0; i < size; i++) {
            for(j = 0; j < size; j++) {
                if (!onlyBlue) rules.add(generateRule(i, j, Color.BLUE, true));
                rules.add(generateRule(i, j, Color.RED, true));
            }
        }

        return rules;
    }

    private static List<Rule> generateRulesFilling(int size) {
        int i, j;
        List<Rule> rules = new ArrayList<>();

        for(i = 0; i < size; i++) {
            for(j = 0; j < size; j++) {
                rules.add(generateRule(i, j, Color.BLUE, false));
                rules.add(generateRule(i, j, Color.RED, false));
            }
        }

        return rules;
    }

    /**
     * Generate an {@link Ohn0Rule} that fills the cell at (i, j) with the given color if the cell is not blank.
     *
     * @param i     X coordinate
     * @param j     Y coordinate
     * @param color Color to fill if not empty
     * @return The corresponding rule
     */
    private static Ohn0Rule generateRule(final int i, final int j, Color color, Boolean heuristicReparation) {
        return new Ohn0Rule(String.format("%s @ (%d,%d)", color.name(), i, j), state -> {
            Board currentBoard = (Board) state;
            if (!heuristicReparation && !currentBoard.getCell(i, j).isBlank()) return Optional.empty();
            Board newBoard = currentBoard.switchColor(i, j, color);
            if (newBoard == null || (!heuristicReparation && !newBoard.isChangeValid(i,j))) {
                return Optional.empty();
            }
            else {
                return Optional.of(newBoard);
            }
        });
    }
}