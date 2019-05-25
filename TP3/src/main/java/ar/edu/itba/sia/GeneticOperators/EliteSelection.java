package ar.edu.itba.sia.GeneticOperators;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EliteSelection implements Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        List<Warrior> selectedWarriors = new ArrayList<>();
        Collections.sort(warriors, new Comparator<Warrior>() {
                    public int compare(Warrior w1, Warrior w2)  {
                        return (int)(w1.getPerformance() - w2.getPerformance());
                    } }
        );

        for(int i = 0; i < quantity; i++ ) {
            selectedWarriors.add(warriors.get(i));
        }

        return selectedWarriors;
    }

}
