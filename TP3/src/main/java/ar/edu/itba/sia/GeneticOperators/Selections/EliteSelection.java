package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class EliteSelection implements Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        warriors.sort(Warrior.BEST_FITNESS_FIRST);

        // subList returns a view (so it can modify the original list). Return a new arrayList so it's independent.
        return new ArrayList<>(warriors.subList(0, quantity));
    }
}
