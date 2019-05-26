package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.Collections;
import java.util.List;

public class StructureEndCondition implements EndCondition {
    private List<Warrior> lastPopulation;

    public StructureEndCondition() {
        lastPopulation = null;
    }

    public boolean evaluate(List<Warrior> population) {
        if(lastPopulation == null) {
            lastPopulation = population;
            return false;
        }
        else {
            return populationHasNotChanged(lastPopulation, population);
        }
    }

    private boolean populationHasNotChanged(List<Warrior> lastPopulation, List<Warrior> currentPopulation) {
        //TODO How to compare if population changed? mean value or intersect both sets?
        return true;
    }
}
