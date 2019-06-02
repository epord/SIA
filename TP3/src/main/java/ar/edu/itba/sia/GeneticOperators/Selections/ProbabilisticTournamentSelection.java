package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Equivalent to a deterministic tournament selection with {@code numPickedWarriors = 2} where there is a set probability
 * for the fittest individual to win a tournament.
 */
public class ProbabilisticTournamentSelection implements Selection {

    private final double p;

    /**
     * @param p Probability of the fittest individual to win a tournament.
     */
    public ProbabilisticTournamentSelection(double p) {
        this.p = p;
    }

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
        return new ArrayList<>(selectedWarriors.subList(0, quantity));
    }

    private Warrior getBestOrWorstWarrior(List<Warrior> population) {
        double r = Math.random();
        population.sort(Warrior.BEST_FITNESS_FIRST);

        return (r < p) ? population.get(0) : population.get(population.size() - 1);
    }
}
