package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;

public abstract class GPSEngineFactory {
    public static GPSEngine getEngineForStrategy(Problem problem, SearchStrategy strategy, Heuristic heuristic) {
        switch (strategy) {
            case BFS:
            case DFS:
            case ASTAR:
            case GREEDY:
                return new GPSEngine(problem, strategy, heuristic);
            case IDDFS:
                return new IddfsGPSEngine(problem, heuristic);
            default:
                throw new IllegalArgumentException("Unknown search strategy " + strategy.name());
        }
    }
}
