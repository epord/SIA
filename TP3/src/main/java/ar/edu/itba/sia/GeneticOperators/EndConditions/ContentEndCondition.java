package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public class ContentEndCondition implements EndCondition {
    private double currentBestPerformance;
    private int maxConsecutiveGenerations;
    private int currentConsecutiveGenerations;

    public ContentEndCondition(int maxConsecutiveGenerations) {
        currentBestPerformance          = -1.0;
        currentConsecutiveGenerations   = 0;
        this.maxConsecutiveGenerations  = maxConsecutiveGenerations;

    }
    public boolean test(List<Warrior> population) {
        double bestPerformance = getBestPerformance(population);

        if(bestPerformance > currentBestPerformance) {
            currentBestPerformance          = bestPerformance;
            currentConsecutiveGenerations   = 0;
            return false;
        }
        else {
            currentConsecutiveGenerations++;
            return currentConsecutiveGenerations >= maxConsecutiveGenerations;
        }
    }

    private double getBestPerformance(List<Warrior> population) {
        population.sort(Warrior.BEST_FITNESS_FIRST);
        return population.get(0).getPerformance();
    }
}
