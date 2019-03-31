package ar.edu.itba.sia.Ohn0;

import ar.edu.itba.sia.Ohn0.Heuristics.ConflictingNumbersHeuristic;
import ar.edu.itba.sia.Ohn0.Heuristics.HeuristicReparationAdmisibleHeuristic;
import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Rule;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws IOException {
        FileManager fm = new FileManager();
        if (args.length >= 1) {
            Settings.loadSettings(args[0]);
        } else {
            Settings.loadSettings();
        }

        Board board = Settings.board;
        Heuristic heuristic = Settings.heuristic;

        GPSEngine engine;
        if (Settings.resolveMethod == ResolveMethod.HEURISTIC_REPARATION) {
            engine = getHeuristicRepairEngine(board, Settings.strategy, heuristic, Settings.fillingMethod);
        } else {
            engine = getFillBlanksEngine(board, Settings.strategy, heuristic);
        }

        long startTime = System.currentTimeMillis();
        engine.findSolution();
        long endTime = System.currentTimeMillis();
        printResults(endTime - startTime, board.getSize(), engine);


//        if (engine.isFailed()) {
//            System.out.println("FAILED");
//        } else {
//            engine.printSolution();
//            fm.writeStringToFile("p5/solution.out", board.getSize() + "\n" + engine.getSolutionNode().getSolution());
//        }
//        System.out.println("\n" + (endTime - startTime) + " ms");
    }

    private static GPSEngine getFillBlanksEngine(Board board, SearchStrategy strategy, Heuristic heuristic) {
        List<Rule> problemRules = generateRulesFilling(board.getSize());
        ProblemImpl OhN0 = new ProblemImpl(board, problemRules);
        return new GPSEngine(OhN0, strategy, heuristic);
    }

    private static GPSEngine getHeuristicRepairEngine(Board board, SearchStrategy strategy, Heuristic heuristic,
                                                      Color fillColor) {
        if(fillColor == Color.BLUE) {
            board = board.fillBlue();
        }
        else if(fillColor == Color.RED) {
            board = board.fillRed();
        }
        else {
            board = board.fillRandomly();
        }

        List<Rule> reparationRules = generateRulesReparation(board.getSize(), fillColor == Color.RED, fillColor == Color.BLUE);
        ProblemImpl OhN0 = new ProblemImpl(board, reparationRules);
        return new GPSEngine(OhN0, strategy, heuristic);

    }

    private static List<Rule> generateRulesReparation(int size, Boolean onlyBlueRules, Boolean onlyRedRules) {
        int i, j;
        List<Rule> rules = new ArrayList<>();
        if(onlyBlueRules && onlyRedRules) {
            throw new IllegalArgumentException();
        }

        for(i = 0; i < size; i++) {
            for(j = 0; j < size; j++) {
                if (!onlyRedRules) {
                    rules.add(generateRule(i, j, Color.BLUE, true));
                }
                if(!onlyBlueRules) {
                    rules.add(generateRule(i, j, Color.RED, true));
                }
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
    private static void printResults(long time, int boardSize,  GPSEngine engine) {
        boolean failed = engine.isFailed();

        System.out.println("Resolve Method: " + Settings.resolveMethod);
        System.out.println("Strategy: " + Settings.strategy.name());
        if(Settings.strategy == SearchStrategy.ASTAR || Settings.strategy == SearchStrategy.GREEDY ) {
            System.out.println("With Heuristic: "  + Settings.heuristicNames.get(Settings.heuristicIndex));
        }
        if(Settings.resolveMethod == ResolveMethod.HEURISTIC_REPARATION) {
            if(Settings.fillingMethod != null) {
                switch (Settings.fillingMethod) {
                    case RED:
                        System.out.println("With filling method of color red");
                        break;
                    case BLUE:
                        System.out.println("With filling method of color blue");
                        break;
                }
            }
            else {
                System.out.println("With random filling method");
            }
        }
        System.out.println("With a board of size: " + boardSize + "X" + boardSize);
        System.out.println("Search was:" + (failed? "Unsuccesful" : "Succesful") );
        if(!failed) {
            System.out.println("You can view the solution steps opening file p5/index.html in your browser");
            System.out.println("Solution Depth was: " + engine.getSolutionNode().getDepth());
            System.out.println("Solution cost was: " + engine.getSolutionNode().getCost());
        }
        System.out.println("Expanded nodes: " + engine.getExplosionCounter());
        System.out.println("Analized states: " + engine.getAnalizedStates());
        System.out.println("Border nodes: " + engine.getBorderNodes());
        System.out.println("time: " + time + "ms");




    }
}
