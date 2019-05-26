package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.Collections;
import java.util.List;

public class NearOptimalEndCondition implements EndCondition {
    private double optimalPerformance;
    private double delta;

    public NearOptimalEndCondition(double optimalPerformance, double delta) {
        this.optimalPerformance = optimalPerformance;
        this.delta              = delta;
    }

    public boolean evaluate(List<Warrior> population) {
        double bestPerformance = getBestPerformance(population);
        return Math.abs(optimalPerformance - bestPerformance) < delta;
    }

    private double getBestPerformance(List<Warrior> population) {
        Collections.sort(population, (w1, w2) -> (int)(w2.getPerformance() - w1.getPerformance()));
        return population.get(0).getPerformance();
    }
}
