package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.Collections;
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
    public boolean evaluate(List<Warrior> population) {
        double bestPerformance = getBestPerformance(population);

        if(bestPerformance > currentBestPerformance) {
            currentBestPerformance          = bestPerformance;
            currentConsecutiveGenerations   = 0;
            return false;
        }
        else {
            currentConsecutiveGenerations ++;
            if(currentConsecutiveGenerations < maxConsecutiveGenerations) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    private double getBestPerformance(List<Warrior> population) {
        Collections.sort(population, (w1, w2) -> (int)(w2.getPerformance() - w1.getPerformance()));
        return population.get(0).getPerformance();
    }
}
