package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EliteSelection implements Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        // sort in descendant order
        Collections.sort(warriors, (w1, w2) -> (int)(w1.getPerformance() - w2.getPerformance()));

        List<Warrior> selectedWarriors = new ArrayList<>();
        for(int i = 0; i < quantity; i++ ) {
            selectedWarriors.add(warriors.get(i));
        }

        return selectedWarriors;
    }

}
