package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class EliteSelection implements Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        // Create a new list so as to not modify the original one
        List<Warrior> sortedWarriors = new ArrayList<>(warriors);
        sortedWarriors.sort(Warrior.BEST_FITNESS_FIRST);
        return new ArrayList<>(warriors.subList(0, quantity));
    }
}
