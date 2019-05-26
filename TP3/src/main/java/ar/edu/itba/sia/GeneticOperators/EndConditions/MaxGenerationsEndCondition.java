package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public class MaxGenerationsEndCondition implements EndCondition {
    private int maxGenerations;
    private int currentGeneration;

    public MaxGenerationsEndCondition(int maxGenerations) {
        this.maxGenerations = maxGenerations;
        currentGeneration   = 0;
    }
    public boolean evaluate(List<Warrior> population) {
        if(currentGeneration < maxGenerations) {
            currentGeneration ++;
            return false;
        }
        else {
            return true;
        }
    }
}
