package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeterministicTournamentSelection implements Selection {
    private int quantityOfChoices;

    public DeterministicTournamentSelection(int quantityOfChoices) {
        this.quantityOfChoices = quantityOfChoices;
    }

    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        List<Warrior> currentChoices;
        List<Warrior> selectedWarriors = new ArrayList<>();
        for(int i = 0; i < quantity; i++) {
            currentChoices = getWarriorsRandomly(quantityOfChoices, warriors);
            selectedWarriors.add(getBestWarrior(currentChoices));
        }
        return selectedWarriors;
    }

    private List<Warrior> getWarriorsRandomly(int quantity, List<Warrior> warriors) {
        List<Warrior> selectedWarriors = new ArrayList<>();
        for(int i = 0; i < quantity; i++) {
            selectedWarriors.add(warriors.get((int)(Math.random() * warriors.size())));
        }

        return selectedWarriors;
    }

    private Warrior getBestWarrior(List<Warrior> population) {
        Collections.sort(population, (w1, w2) -> (int)(w2.getPerformance() - w1.getPerformance()));
        return population.get(0);
    }
}
