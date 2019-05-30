package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProbabilisticTournamentSelection implements Selection {

    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        List<Warrior> currentChoices;
        List<Warrior> selectedWarriors = new ArrayList<>();
        for(int i = 0; i < quantity; i++) {
            currentChoices = getWarriorsRandomly(2, warriors);
            selectedWarriors.add(getBestOrWorstWarrior(currentChoices));
        }
        return selectedWarriors;
    }

    private List<Warrior> getWarriorsRandomly(int quantity, List<Warrior> warriors) {
        List<Warrior> selectedWarriors = new ArrayList<>(warriors);
        Collections.shuffle(selectedWarriors);
        return selectedWarriors.subList(0, quantity);
    }

    private Warrior getBestOrWorstWarrior(List<Warrior> population) {
        double r = Math.random();
        population.sort(Warrior.BEST_FITNESS_FIRST);

        // TODO read 0.75 from settings
        return (r >= 0.75) ? population.get(population.size() - 1) : population.get(0);
    }
}
