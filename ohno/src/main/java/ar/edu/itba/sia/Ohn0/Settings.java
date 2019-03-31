package ar.edu.itba.sia.Ohn0;

import ar.edu.itba.sia.Ohn0.Heuristics.*;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

public class Settings {
    public static SearchStrategy strategy;
    public static Heuristic heuristic;
    public static Board board;
    public static Color fillingMethod;
    public static ResolveMethod resolveMethod;

    private static Map<String, SearchStrategy> strategyMap= new HashMap<>();
    private static Map<String, Heuristic> heuristicMap= new HashMap<>();
    static {
        strategyMap.put("BFS", SearchStrategy.BFS);
        strategyMap.put("DFS", SearchStrategy.DFS);
        strategyMap.put("IDDFS", SearchStrategy.IDDFS);
        strategyMap.put("GREEDY", SearchStrategy.GREEDY);
        strategyMap.put("ASTAR", SearchStrategy.ASTAR);

        heuristicMap.put("0", new FillingBlanksHeuristic());
        heuristicMap.put("1", new ConflictingNumbersHeuristic());
        heuristicMap.put("2", new FillBlanksNonTrivialAdmisibleHeuristic());
        heuristicMap.put("3", new HeuristicReparationAdmisibleHeuristic());
        heuristicMap.put("4", new AverageRedsHeuristic());
        heuristicMap.put("5", new MissingRedsHeuristics());
        heuristicMap.put("6", new MissingVisibleBlueHeuristics());
        heuristicMap.put("7", new AddAllHeuristics());
    }

    public static void loadSettings() throws FileNotFoundException, IOException {
        Settings.loadSettings("settings.properties");
    }

    public static void loadSettings(String propertiesPath) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(Paths.get(propertiesPath).toFile()));
        } catch (Exception e) {
            throw new InvalidPathException(Paths.get(propertiesPath).toAbsolutePath().toString(), "Properties file does not exist.");
        }

        String inputStrategy = properties.getProperty("strategy");
        String inputHeuristic = properties.getProperty("heuristic");
        String inputResolveMethod = properties.getProperty("resolveMethod");
        String inputFillingMethod = properties.getProperty("fillingMethod");
        String inputBoardPath = properties.getProperty("board");
        if (inputStrategy == null || inputResolveMethod == null || inputBoardPath == null) {
            throw new InvalidPropertiesFormatException("Invalid properties.");
        }

        if (!strategyMap.containsKey(inputStrategy)) throw new IllegalArgumentException("Invalid inputStrategy name.");
        strategy = strategyMap.get(inputStrategy);

        if ((inputHeuristic != null && !heuristicMap.containsKey(inputHeuristic))
                || (inputStrategy == null && (strategy == SearchStrategy.GREEDY || strategy == SearchStrategy.ASTAR))){
            throw new IllegalArgumentException("Invalid heuristic name.");
        }
        heuristic = heuristicMap.get(inputHeuristic);

        FileManager fm = new FileManager();
        try {
            board = fm.readStateFromFile(Paths.get(inputBoardPath));
        } catch (Exception e) {
            throw new InvalidPathException(Paths.get(inputBoardPath).toAbsolutePath().toString(), "File does not exist.");
        }

        switch (inputResolveMethod) {
            case "0":
                resolveMethod = ResolveMethod.FILL_BLANKS;
                break;
            case "1":
                resolveMethod = ResolveMethod.HEURISTIC_REPARATION;
                break;
        }

        switch (inputFillingMethod) {
            case "red":
            case "RED":
                fillingMethod = Color.RED;
                break;
            case "blue":
            case "BLUE":
                fillingMethod = Color.BLUE;
                break;
            case "random":
            case "RANDOM":
                fillingMethod = null;
                break;
            default:
                if (resolveMethod == ResolveMethod.HEURISTIC_REPARATION) {
                    throw new IllegalArgumentException("Invalid filling method.");
                }
        }

        String[] fillBlanksValidHeuristics = {"0", "2", "4", "5", "6", "7"};
        String[] heuristicReparationValidHeuristics = {"1", "3", "5", "6"};
        if (resolveMethod == ResolveMethod.FILL_BLANKS && Arrays.stream(fillBlanksValidHeuristics).noneMatch(h -> h.equals(inputHeuristic))) {
            throw new IllegalArgumentException("Can't use this heuristic with '" + resolveMethod.toString() + "'");
        }
        if (resolveMethod == ResolveMethod.HEURISTIC_REPARATION && Arrays.stream(heuristicReparationValidHeuristics).noneMatch(h -> h.equals(inputHeuristic))) {
            throw new IllegalArgumentException("Can't use this heuristic with '" + resolveMethod.toString() + "'");
        }


    }
}
