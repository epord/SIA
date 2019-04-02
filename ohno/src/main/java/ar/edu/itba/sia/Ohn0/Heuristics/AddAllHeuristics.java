package ar.edu.itba.sia.Ohn0.Heuristics;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

import java.util.ArrayList;
import java.util.List;

public class AddAllHeuristics implements Heuristic {

    @Override
    public Integer getValue(State state) {
        List<Heuristic> heuristics = new ArrayList<>();
        heuristics.add(new AverageRedsHeuristic());
        heuristics.add(new FillingBlanksHeuristic());
        heuristics.add(new MissingRedsHeuristics());
        heuristics.add(new MissingVisibleBlueHeuristics());
        int value = 0;
        for(Heuristic h : heuristics) {
            value =+ h.getValue(state) * 100;
        }

        return value;
    }

    public String getName() {
        return "7(Add All Heuristics)";
    }

    public String toString() {
        return getName();
    }
}
