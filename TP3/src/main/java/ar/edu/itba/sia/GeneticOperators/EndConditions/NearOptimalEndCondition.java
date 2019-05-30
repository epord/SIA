package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.Collections;
import java.util.List;

public class NearOptimalEndCondition implements EndCondition {
    private double optimalFitness;
    private double delta;

    public NearOptimalEndCondition(double optimalFitness, double delta) {
        this.optimalFitness = optimalFitness;
        this.delta              = delta;
    }

    public boolean test(List<Warrior> population) {
        double bestFitness = getBestFitness(population);
        return Math.abs(optimalFitness - bestFitness) < delta;
    }

    private double getBestFitness(List<Warrior> population) {
        population.sort(Warrior.BEST_FITNESS_FIRST);
        return population.get(0).getFitness();
    }
}
