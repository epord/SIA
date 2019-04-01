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

    static {
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

    public static void main(String[] args) throws IOException {
        generateFillingMethodGraph();
    }

    private static void generateHeuristicGraph() {
        Board board;
        Heuristic heuristic;
        SearchStrategy strategy;
        GPSEngine engine;
        FileManager fm = new FileManager();

        for (int size = 4; size <= 9; size++) {

        }

    }

    private static void generateFillingMethodGraph() throws IOException {
        Board board;
        Heuristic heuristic;
        SearchStrategy strategy;
        GPSEngine engine;
        FileManager fm = new FileManager();
        List<Long> ASTARtimes = new ArrayList<>();
        Long startTime;

        for (int colorIdx = 0; colorIdx < colors.size(); colorIdx++) {
            System.out.println("Running with color: " + colors.get(colorIdx));
            for (int i = 1; i <= 5; i++) {
                board = fm.readStateFromFile(Paths.get("boards", "board6x6_" + i));

                // ASTAR
                engine = Main.getHeuristicRepairEngine(board, SearchStrategy.ASTAR, new ConflictingNumbersHeuristic(), colors.get(colorIdx));
                startTime = System.currentTimeMillis();
                engine.findSolution();
                ASTARtimes.add(System.currentTimeMillis() - startTime);
                System.out.println("ASTAR finished " + i);
            }
            String color = colors.get(colorIdx) == null ? "RANDOM" : colors.get(colorIdx).toString() ;
            saveResults("fillMethodMetric.m", ASTARtimes, "ASTARtimes" + color, true);
            ASTARtimes.clear();
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
//  1. comparar heurísticas (ASTAR y GREEDY) -> 2 gráficos
//            size fijo,
//  2. comparar fillblue/red/random
//  3. comparar métodos de búsqueda (BFS vs DFS vs IDDFS vs GREEDY vs ASTAR) con sus mejores heurísitcas y métodos -> 2 gráficos: fillbanks y heuristicReparation
//  4.
}

