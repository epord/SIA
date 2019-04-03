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
    private static List<RunConfiguration> runConfigurations = new ArrayList<>();

    static {
        runConfigurations.add(new RunConfiguration(SearchStrategy.BFS, null, null));
        runConfigurations.add(new RunConfiguration(SearchStrategy.DFS, null, null));
        runConfigurations.add(new RunConfiguration(SearchStrategy.IDDFS, null, null));
        runConfigurations.add(new RunConfiguration(SearchStrategy.GREEDY, new ConflictingNumbersHeuristic(), Color.BLUE));
        runConfigurations.add(new RunConfiguration(SearchStrategy.ASTAR, new ConflictingNumbersHeuristic(), Color.BLUE));

        strategies.add(SearchStrategy.BFS);
        strategies.add(SearchStrategy.DFS);
        strategies.add(SearchStrategy.IDDFS);
        strategies.add(SearchStrategy.GREEDY);
        strategies.add(SearchStrategy.ASTAR);


        heuristics.add(new FillingBlanksHeuristic());
        heuristics.add(new ConflictingNumbersHeuristic());
        heuristics.add(new FillBlanksNonTrivialAdmisibleHeuristic());
        heuristics.add(new HeuristicReparationAdmisibleHeuristic());
        heuristics.add(new ApproximateRedsHeuristic());
        heuristics.add(new MissingRedsHeuristics());
        heuristics.add(new MissingVisibleBlueHeuristics());
        heuristics.add(new AddAllHeuristics());

        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(null);
    }

    private static class RunConfiguration {
        SearchStrategy searchStrategy;
        Heuristic heuristic;
        Color fillingMethod;

        public RunConfiguration(SearchStrategy searchStrategy, Heuristic heuristic, Color fillingMethod) {
            this.searchStrategy = searchStrategy;
            this.heuristic = heuristic;
            this.fillingMethod = fillingMethod;
        }

        public GPSEngine getFillBlanksEngine(Board board) {
            return Main.getFillBlanksEngine(board, searchStrategy, new FillingBlanksHeuristic());
        }

        public GPSEngine getHeuristicReparationEngine(Board board) {
            return Main.getHeuristicRepairEngine(board, searchStrategy, new ConflictingNumbersHeuristic(), Color.BLUE);
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

//        System.out.println("Comparing filling methods...");
//        generateFillingMethodGraph(10);
//        System.out.println("\n\nComparing heuristics...");
//        generateHeuristicMethodGraph(10);
//        System.out.println("\n\nComparing search strategies with filling blanks...");
//        generateFillBlanksSearchStrategyGraph(10);
//        System.out.println("\n\nComparing search strategies with heuristic reparation...");
//        generateHeuristicReparationSearchStrategyGraph(10);
        compareASTARGraph(5);

        System.out.println("\n\nMetrics generated: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * Compares filling blue/red/random with ASTAR
     * @throws IOException
     */
    private static void generateFillingMethodGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> ASTARtimes = new ArrayList<>();
        List<Long> analizedStates = new ArrayList<>();
        List<Long> borderNodes = new ArrayList<>();
        List<Long> explosionCounter = new ArrayList<>();
        List<Integer> costs = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
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
                analizedStates.add(engine.getAnalizedStates());
                borderNodes.add(engine.getBorderNodes());
                explosionCounter.add(engine.getExplosionCounter());
                costs.add(engine.getSolutionNode().getCost());
                depths.add(engine.getSolutionNode().getDepth());

                System.out.println("ASTAR finished " + i);
            }
            String colorName = color == null ? "RANDOM" : color.toString() ;
            saveResults("metrics/fillMethodMetric.m", ASTARtimes, colorName + "_time", colorId++ != 0);
            saveResults("metrics/fillMethodMetric.m", analizedStates, colorName + "_analizedStates", true);
            saveResults("metrics/fillMethodMetric.m", borderNodes, colorName + "_borderNodes", true);
            saveResults("metrics/fillMethodMetric.m", explosionCounter, colorName + "_explosionCounter", true);
            saveResults("metrics/fillMethodMetric.m", costs, colorName + "_costs", true);
            saveResults("metrics/fillMethodMetric.m", depths, colorName + "_depths", true);
            ASTARtimes.clear();
            analizedStates.clear();
            borderNodes.clear();
            explosionCounter.clear();
            costs.clear();
            depths.clear();
        }
    }

    /**
     * Test all heuristics with ASTAR, fill blue, heuristic reparation
     * @param boardCount
     * @throws IOException
     */
    private static void generateHeuristicMethodGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> heuristicTimes = new ArrayList<>();
        List<Long> analizedStates = new ArrayList<>();
        List<Long> borderNodes = new ArrayList<>();
        List<Long> explosionCounter = new ArrayList<>();
        List<Integer> costs = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        long startTime;
        int heuristicId = 0;

        for (Heuristic heuristic: heuristics) {
            System.out.println("Running with heuristic: " + heuristic);
            for (int i = 1; i <= boardCount ; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board5x5_" + i));
                engine = Main.getHeuristicRepairEngine(board, SearchStrategy.ASTAR, heuristic, Color.BLUE);
                startTime = System.currentTimeMillis();

                engine.findSolution();

                heuristicTimes.add(System.currentTimeMillis() - startTime);
                analizedStates.add(engine.getAnalizedStates());
                borderNodes.add(engine.getBorderNodes());
                explosionCounter.add(engine.getExplosionCounter());
                costs.add(engine.getSolutionNode().getCost());
                depths.add(engine.getSolutionNode().getDepth());

                System.out.println("ASTAR finished " + i);
            }
            saveResults("metrics/heuristicMetric.m", heuristicTimes, "heuristic" + heuristicId + "_time", heuristicId != 0);
            saveResults("metrics/heuristicMetric.m", analizedStates, "heuristic" + heuristicId + "_analizedStates", true);
            saveResults("metrics/heuristicMetric.m", borderNodes, "heuristic" + heuristicId + "_borderNodes", true);
            saveResults("metrics/heuristicMetric.m", explosionCounter, "heuristic" + heuristicId + "_explosionCounter", true);
            saveResults("metrics/heuristicMetric.m", costs, "heuristic" + heuristicId + "_costs", true);
            saveResults("metrics/heuristicMetric.m", depths, "heuristic" + heuristicId + "_depths", true);
            heuristicId++;
            heuristicTimes.clear();
            analizedStates.clear();
            borderNodes.clear();
            explosionCounter.clear();
            costs.clear();
            depths.clear();
        }
    }

    /**
     * Test all search strategies with fill blanks
     * @param boardCount
     * @throws IOException
     */
    private static void generateFillBlanksSearchStrategyGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> runningTimes = new ArrayList<>();
        List<Long> analizedStates = new ArrayList<>();
        List<Long> borderNodes = new ArrayList<>();
        List<Long> explosionCounter = new ArrayList<>();
        List<Integer> costs = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        long startTime;
        int strategyId = 0;

        for (RunConfiguration runConfiguration: runConfigurations) {
            System.out.println("Running with search strategy " + runConfiguration.searchStrategy.name());
            for (int i = 1; i <= boardCount; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board4x4_" + i));
                engine = runConfiguration.getFillBlanksEngine(board);
                startTime = System.currentTimeMillis();

                engine.findSolution();
                runningTimes.add(System.currentTimeMillis() - startTime);
                analizedStates.add(engine.getAnalizedStates());
                borderNodes.add(engine.getBorderNodes());
                explosionCounter.add(engine.getExplosionCounter());
                costs.add(engine.getSolutionNode().getCost());
                depths.add(engine.getSolutionNode().getDepth());

                System.out.println(runConfiguration.searchStrategy.name() + " finished " + i);
            }
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", runningTimes, runConfiguration.searchStrategy.name() + "_time", strategyId++ != 0);
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", analizedStates, runConfiguration.searchStrategy.name() + "_analizedStates", true);
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", borderNodes, runConfiguration.searchStrategy.name() + "_borderNodes", true);
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", explosionCounter, runConfiguration.searchStrategy.name() + "_explosionCounter", true);
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", costs, runConfiguration.searchStrategy.name() + "_costs", true);
            saveResults("metrics/fillBlanksSearchStrategiesMetric.m", depths, runConfiguration.searchStrategy.name() + "_depths", true);
            runningTimes.clear();
            analizedStates.clear();
            borderNodes.clear();
            explosionCounter.clear();
            costs.clear();
            depths.clear();
        }
    }

    /**
     * Test all search strategies with heuristic reparation
     * @param boardCount
     * @throws IOException
     */
    private static void generateHeuristicReparationSearchStrategyGraph(int boardCount) throws IOException {
        Board board;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> runningTimes = new ArrayList<>();
        List<Long> analizedStates = new ArrayList<>();
        List<Long> borderNodes = new ArrayList<>();
        List<Long> explosionCounter = new ArrayList<>();
        List<Integer> costs = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        long startTime;
        int strategyId = 0;

        for (RunConfiguration runConfiguration: runConfigurations) {
            System.out.println("Running with search strategy " + runConfiguration.searchStrategy.name());
            for (int i = 1; i <= boardCount; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board5x5_" + i));
                engine = runConfiguration.getHeuristicReparationEngine(board);
                startTime = System.currentTimeMillis();

                engine.findSolution();

                runningTimes.add(System.currentTimeMillis() - startTime);
                analizedStates.add(engine.getAnalizedStates());
                borderNodes.add(engine.getBorderNodes());
                explosionCounter.add(engine.getExplosionCounter());
                costs.add(engine.getSolutionNode().getCost());
                depths.add(engine.getSolutionNode().getDepth());

                System.out.println(runConfiguration.searchStrategy.name() + " finished " + i);
            }
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", runningTimes, runConfiguration.searchStrategy.name() + "_time", strategyId++ != 0);
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", analizedStates, runConfiguration.searchStrategy.name() + "_analizedStates", true);
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", borderNodes, runConfiguration.searchStrategy.name() + "_borderNodes", true);
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", explosionCounter, runConfiguration.searchStrategy.name() + "_explosionCounter", true);
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", costs, runConfiguration.searchStrategy.name() + "_costs", true);
            saveResults("metrics/heuristicReparationSearchStrategiesMetric.m", depths, runConfiguration.searchStrategy.name() + "_depths", true);
            runningTimes.clear();
            analizedStates.clear();
            borderNodes.clear();
            explosionCounter.clear();
            costs.clear();
            depths.clear();
        }
    }


    /**
     * Test all search strategies with heuristic reparation
     * @param boardCount
     * @throws IOException
     */
    private static void compareASTARGraph(int boardCount) throws IOException {
        Board board5x5, board6x6, board7x7, board8x8, board9x9;
        GPSEngine engine5x5, engine6x6, engine7x7, engine8x8, engine9x9;
        FileManager fm = new FileManager();
        List<Long> runningTimes5x5 = new ArrayList<>();
        List<Long> runningTimes6x6 = new ArrayList<>();
        List<Long> runningTimes7x7 = new ArrayList<>();
        List<Long> runningTimes8x8 = new ArrayList<>();
        List<Long> runningTimes9x9 = new ArrayList<>();
        long startTime;
        int strategyId = 0;

        RunConfiguration runConfiguration = new RunConfiguration(SearchStrategy.ASTAR, new ConflictingNumbersHeuristic(), Color.BLUE);

        for (int i = 1; i <= boardCount; i++) {
            board5x5 = fm.readStateFromFile(Paths.get("boards", "board5x5_" + i));
            board6x6 = fm.readStateFromFile(Paths.get("boards", "board6x6_" + i));
            board7x7 = fm.readStateFromFile(Paths.get("boards", "board7x7_" + i));
            board8x8 = fm.readStateFromFile(Paths.get("boards", "board8x8_" + i));
            board9x9 = fm.readStateFromFile(Paths.get("boards", "board9x9_" + i));

            engine5x5 = runConfiguration.getHeuristicReparationEngine(board5x5);
            engine6x6 = runConfiguration.getHeuristicReparationEngine(board6x6);
            engine7x7 = runConfiguration.getHeuristicReparationEngine(board7x7);
            engine8x8 = runConfiguration.getHeuristicReparationEngine(board8x8);
            engine9x9 = runConfiguration.getHeuristicReparationEngine(board9x9);

            startTime = System.currentTimeMillis();
            engine5x5.findSolution();
            runningTimes5x5.add(System.currentTimeMillis() - startTime);
            System.out.println("5x5 solved");

            startTime = System.currentTimeMillis();
            engine6x6.findSolution();
            runningTimes6x6.add(System.currentTimeMillis() - startTime);
            System.out.println("6x6 solved");

            startTime = System.currentTimeMillis();
            engine7x7.findSolution();
            runningTimes7x7.add(System.currentTimeMillis() - startTime);
            System.out.println("7x7 solved");

            startTime = System.currentTimeMillis();
            engine8x8.findSolution();
            runningTimes8x8.add(System.currentTimeMillis() - startTime);
            System.out.println("8x8 solved");

            startTime = System.currentTimeMillis();
            engine9x9.findSolution();
            runningTimes9x9.add(System.currentTimeMillis() - startTime);
            System.out.println("9x9 solved");

            System.out.println(runConfiguration.searchStrategy.name() + " finished " + i);
        }
        saveResults("metrics/compareASTAR.m", runningTimes5x5, runConfiguration.searchStrategy.name() + "5x5_time", strategyId++ != 0);
        saveResults("metrics/compareASTAR.m", runningTimes6x6, runConfiguration.searchStrategy.name() + "6x6_time", strategyId++ != 0);
        saveResults("metrics/compareASTAR.m", runningTimes7x7, runConfiguration.searchStrategy.name() + "7x7_time", strategyId++ != 0);
        saveResults("metrics/compareASTAR.m", runningTimes8x8, runConfiguration.searchStrategy.name() + "8x8_time", strategyId++ != 0);
        saveResults("metrics/compareASTAR.m", runningTimes9x9, runConfiguration.searchStrategy.name() + "9x9_time", strategyId++ != 0);
        runningTimes5x5.clear();
        runningTimes6x6.clear();
        runningTimes7x7.clear();
        runningTimes8x8.clear();
        runningTimes9x9.clear();
    }

    private static <T> void saveResults(String path, List<T> list, String listName, Boolean append) throws IOException {
        FileManager fm = new FileManager();

        // Values
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

        // MEAN
        builder
            .append(listName)
            .append("_mean=mean(")
            .append(listName)
            .append(");\n");

        // STD
        builder
            .append(listName)
            .append("_std=std(")
            .append(listName)
            .append(");\n");
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

