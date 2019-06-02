package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeterministicTournamentSelection implements Selection {
    private int numPickedWarriors;

    /**
     * @param numPickedWarriors Number of warriors to randomly on every tournament. Of these, the best one will win.
     */
    public DeterministicTournamentSelection(int numPickedWarriors) {
        this.numPickedWarriors = numPickedWarriors;
    }

    public List<Warrior> select(List<Warrior> warriors, int numTournaments) {
        List<Warrior> currentChoices;
        List<Warrior> selectedWarriors = new ArrayList<>();
        for(int i = 0; i < numTournaments; i++) {
            currentChoices = getWarriorsRandomly(numPickedWarriors, warriors);
            selectedWarriors.add(getBestWarrior(currentChoices));
        }
        return selectedWarriors;
    }

    private List<Warrior> getWarriorsRandomly(int quantity, List<Warrior> warriors) {
        List<Warrior> selectedWarriors = new ArrayList<>(warriors);
        Collections.shuffle(selectedWarriors);
        return new ArrayList<>(selectedWarriors.subList(0, quantity));
    }

    private Warrior getBestWarrior(List<Warrior> population) {
        population.sort(Warrior.BEST_FITNESS_FIRST);
        return population.get(0);
    }
}
