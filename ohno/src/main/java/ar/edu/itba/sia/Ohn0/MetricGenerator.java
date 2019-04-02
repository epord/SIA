package ar.edu.itba.sia.Ohn0;

import ar.edu.itba.sia.Ohn0.Heuristics.*;
import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class MetricGenerator {

    private static List<SearchStrategy> strategies = new ArrayList<>();
    private static List<Heuristic> heuristics = new ArrayList<>();
    private static List<Color> colors = new ArrayList<>();
    private static List<RunConfigurations> runConfigurations = new ArrayList<>();

    static {
        runConfigurations.add(new RunConfigurations(SearchStrategy.BFS, null, null));
        runConfigurations.add(new RunConfigurations(SearchStrategy.DFS, null, null));
        runConfigurations.add(new RunConfigurations(SearchStrategy.IDDFS, null, null));
        runConfigurations.add(new RunConfigurations(SearchStrategy.GREEDY, new ConflictingNumbersHeuristic(), Color.BLUE));
        runConfigurations.add(new RunConfigurations(SearchStrategy.ASTAR, new ConflictingNumbersHeuristic(), Color.BLUE));

        strategies.add(SearchStrategy.BFS);
        strategies.add(SearchStrategy.DFS);
        strategies.add(SearchStrategy.IDDFS);
        strategies.add(SearchStrategy.GREEDY);
        strategies.add(SearchStrategy.ASTAR);


        heuristics.add(new FillingBlanksHeuristic());
        heuristics.add(new ConflictingNumbersHeuristic());
        heuristics.add(new FillBlanksNonTrivialAdmisibleHeuristic());
        heuristics.add(new HeuristicReparationAdmisibleHeuristic());
        heuristics.add(new AverageRedsHeuristic());
        heuristics.add(new MissingRedsHeuristics());
        heuristics.add(new MissingVisibleBlueHeuristics());
        heuristics.add(new AddAllHeuristics());

        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(null);
    }

    private static class RunConfigurations {
        SearchStrategy searchStrategy;
        Heuristic heuristic;
        Color fillingMethod;

        public RunConfigurations(SearchStrategy searchStrategy, Heuristic heuristic, Color fillingMethod) {
            this.searchStrategy = searchStrategy;
            this.heuristic = heuristic;
            this.fillingMethod = fillingMethod;
        }

        public GPSEngine getFillBlanksEngine(Board board) {
            return Main.getHeuristicRepairEngine(board, searchStrategy, new FillingBlanksHeuristic(), Color.BLUE);
        }

        public GPSEngine getHeuristicReparationEngine(Board board) {
            return Main.getHeuristicRepairEngine(board, searchStrategy, new ConflictingNumbersHeuristic(), Color.BLUE);
        }
    }

    public static void main(String[] args) throws IOException {
//        generateFillingMethodGraph(5);
//        generateHeuristicMethodGraph(5);
        generateFillBlanksSearchStrategyGraph(5);
        generateHeuristicReparationSearchStrategyGraph(5);
    }

    private static void generateFillBlanksSearchStrategyGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> runningTimes = new ArrayList<>();
        long startTime;
        int strategyId = 0;

        for (RunConfigurations runConfiguration: runConfigurations) {
            System.out.println("Running with search strategy " + runConfiguration.searchStrategy.name());
            for (int i = 2; i <= boardCount; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board4x4_" + i));
                engine = runConfiguration.getFillBlanksEngine(board);
                startTime = System.currentTimeMillis();

                engine.findSolution();
                runningTimes.add(System.currentTimeMillis() - startTime);
                System.out.println(runConfiguration.searchStrategy.name() + " finished " + i);
            }
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", runningTimes, runConfiguration.searchStrategy.name(), strategyId++ != 0);
            runningTimes.clear();
        }
    }

    private static void generateHeuristicReparationSearchStrategyGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> runningTimes = new ArrayList<>();
        long startTime;
        int strategyId = 0;

        for (RunConfigurations runConfiguration: runConfigurations) {
            System.out.println("Running with search strategy " + runConfiguration.searchStrategy.name());
            for (int i = 2; i <= boardCount; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board5x5_" + i));
                engine = runConfiguration.getHeuristicReparationEngine(board);
                startTime = System.currentTimeMillis();

                engine.findSolution();
                runningTimes.add(System.currentTimeMillis() - startTime);
                System.out.println(runConfiguration.searchStrategy.name() + " finished " + i);
            }
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", runningTimes, runConfiguration.searchStrategy.name(), strategyId++ != 0);
            runningTimes.clear();
        }
    }

    /**
     * Compares filling blue/red/random with ASTAR in 6x6 boards
     * @throws IOException
     */
    private static void generateFillingMethodGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> ASTARtimes = new ArrayList<>();
        long startTime;
        int colorId = 0;

        for (Color color: colors) {
            System.out.println("Running with color: " + color);
            for (int i = 1; i <= boardCount; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board6x6_" + i));
                engine = Main.getHeuristicRepairEngine(board, SearchStrategy.ASTAR, new ConflictingNumbersHeuristic(), color);
                startTime = System.currentTimeMillis();

                engine.findSolution();

                ASTARtimes.add(System.currentTimeMillis() - startTime);
                System.out.println("ASTAR finished " + i);
            }
            String colorName = color == null ? "RANDOM" : color.toString() ;
            saveResults("metrics/fillMethodMetric.m", ASTARtimes, "ASTARtimes" + colorName, colorId++ != 0);
            ASTARtimes.clear();
        }
    }

    /**
     * Test all heuristics with ASTAR, fill blue, heuristic reparation with 7x7 boards
     * @param boardCount
     * @throws IOException
     */
    private static void generateHeuristicMethodGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> heuristicTimes = new ArrayList<>();
        long startTime;
        int heuristicId = 0;

        for (Heuristic heuristic: heuristics) {
            System.out.println("Running with heuristic: " + heuristic);
            for (int i = 1; i <= boardCount ; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board5x5_" + i));
                engine = Main.getHeuristicRepairEngine(board, SearchStrategy.ASTAR, heuristic, Color.BLUE);
                startTime = System.currentTimeMillis();

                engine.findSolution();;

                heuristicTimes.add(System.currentTimeMillis() - startTime);
                System.out.println("ASTAR finished " + i);
            }
            saveResults("metrics/heuristicMetric.m", heuristicTimes, "heuristic" + heuristicId, heuristicId++ != 0);
            heuristicTimes.clear();
        }
    }

    private static <T> void saveResults(String path, List<T> list, String listName, Boolean append) throws IOException {
        FileManager fm = new FileManager();
        StringBuilder builder = new StringBuilder();
        builder.append(listName);
        builder.append("=[");
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("];\n");
        fm.writeStringToFile(path, builder.toString(), append);
    }

//    Output:
//        5x5 | DFS | 4 corridas | 10 boards:
//              time = [ [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..]]
//              explosionNumber, anailzedNodes, borderNode, cost, depth
//
//        5x5 | ASTAR | fillBlanks con FillBLanksHeuristic | 4 corridas | 10 boards:
//              time = [ [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..]]
//              explosionNumber, anailzedNodes, borderNode, cost, depth
//
//        5x5 | ASTAR | heuritic reparation  con ConflictingNumbers y fillBlue | 1 corridas | 10 boards:
//              time = [ [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..],
//                       [ 20, 25, 23, 30, 30 ..]]
//              explosionNumber, anailzedNodes, borderNode, cost, depth





// Graficos
//  1. comparar heurísticas (ASTAR) -> 2 gráficos
//            size fijo,
//  2. comparar fillblue/red/random
//  3. comparar métodos de búsqueda (BFS vs DFS vs IDDFS vs GREEDY vs ASTAR) con sus mejores heurísitcas y métodos -> 2 gráficos: fillbanks y heuristicReparation
//  4.
}

