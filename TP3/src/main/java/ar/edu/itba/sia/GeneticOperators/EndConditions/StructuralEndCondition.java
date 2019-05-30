package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Structural end condition.  Condition is met when a specified portion of the population does not change from one
 * generation to the next.
 */
public class StructuralEndCondition implements EndCondition {
    private final double requiredPercentage;
    private List<Warrior> lastPopulation;
//    private Map<Warrior, Integer> lastPopulationMap;

    /**
     * @param requiredPercentage Minimum percentage of the population that is required to change generation over generation.
     *                           If this percentage is not reached, the condition is reached.
     */
    public StructuralEndCondition(double requiredPercentage) {
        this.requiredPercentage = requiredPercentage;
    }

    public boolean test(List<Warrior> population) {
        if (lastPopulation == null) {
            lastPopulation = population;
//            lastPopulationMap = fillOccurrenceMap(population);
            return false;
        }
        else {
            boolean end = percentageDifference(lastPopulation, population) < requiredPercentage;
            lastPopulation = population;
            return end;
        }
    }

    private double percentageDifference(List<Warrior> lastPopulation, List<Warrior> currentPopulation) {
        List<Warrior> difference;
        int totalSize;
        if (lastPopulation.size() > currentPopulation.size()) {
            difference = new ArrayList<>(lastPopulation);
            difference.removeAll(currentPopulation);
            totalSize = lastPopulation.size();
        } else {
            difference = new ArrayList<>(currentPopulation);
            difference.removeAll(lastPopulation);
            totalSize = currentPopulation.size();
        }

        return (double) difference.size() / totalSize;

        // TODO try to use this code but instead of calculating whether population is different or not, compute HOW different they are
//        Map<Warrior, Integer> currentPopulationMap = fillOccurrenceMap(currentPopulation); // This step is always required otherwise the map will get outdated
//        if (currentPopulation.size() != lastPopulation.size()) {
//            lastPopulationMap = currentPopulationMap;
//            return false;
//        }
//        boolean result = lastPopulationMap.equals(currentPopulationMap);
//        lastPopulationMap = currentPopulationMap;
//        return result;
    }

    /**
     * Create a map of Warrior => occurrences. Start with 1 occurrence and add for every new one.
     *
     * @param population Warrior population to convert to map.
     * @return The resulting map.
     */
    private Map<Warrior, Integer> fillOccurrenceMap(List<Warrior> population) {
        Map<Warrior, Integer> result = new HashMap<>(population.size());
        population.forEach(w -> result.merge(w, 1, Integer::sum));
        return result;
    }
}
