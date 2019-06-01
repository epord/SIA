package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.Arrays;
import java.util.List;

public class EndConditionsCombiner implements EndCondition {
    private List<EndCondition> endConditions;

    public EndConditionsCombiner(EndCondition... endConditions) {
        this.endConditions = Arrays.asList(endConditions);
    }

    @Override
    public boolean test(List<Warrior> population) {
        return endConditions.stream().anyMatch(c -> c.test(population));
    }
}
