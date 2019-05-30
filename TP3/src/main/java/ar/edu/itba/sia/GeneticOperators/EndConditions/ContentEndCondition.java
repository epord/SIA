package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public class ContentEndCondition implements EndCondition {
    private double currentBestFitness;
    private int maxConsecutiveGenerations;
    private int currentConsecutiveGenerations;

    public ContentEndCondition(int maxConsecutiveGenerations) {
        currentBestFitness          = -1.0;
        currentConsecutiveGenerations   = 0;
        this.maxConsecutiveGenerations  = maxConsecutiveGenerations;

    }
    public boolean test(List<Warrior> population) {
        double bestFitness = getBestFitness(population);

        if (bestFitness > currentBestFitness) {
            currentBestFitness          = bestFitness;
            currentConsecutiveGenerations   = 0;
            return false;
        }
        else {
            return ++currentConsecutiveGenerations >= maxConsecutiveGenerations;
        }
    }

    private double getBestFitness(List<Warrior> population) {
        population.sort(Warrior.BEST_FITNESS_FIRST);
        return population.get(0).getFitness();
    }
}
