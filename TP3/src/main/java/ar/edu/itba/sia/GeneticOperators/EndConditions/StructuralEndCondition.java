package ar.edu.itba.sia.GeneticOperators.EndConditions;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.util.WarriorUtils;

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
            if (end) {
                System.out.println("Structural end");
            }
            return end;
        }
    }

//    private double percentageDifference(List<Warrior> lastPopulation, List<Warrior> currentPopulation) {
//        List<Warrior> difference;
//        int totalSize;
//        if (lastPopulation.size() > currentPopulation.size()) {
//            difference = new ArrayList<>(lastPopulation);
//            difference.removeAll(currentPopulation);
//            totalSize = lastPopulation.size();
//        } else {
//            difference = new ArrayList<>(currentPopulation);
//            difference.removeAll(lastPopulation);
//            totalSize = currentPopulation.size();
//        }
//
//        return (double) difference.size() / totalSize;
//
//        // Map<Warrior, Integer> currentPopulationMap = fillOccurrenceMap(currentPopulation); // This step is always required otherwise the map will get outdated
////        if (currentPopulation.size() != lastPopulation.size()) {
////            lastPopulationMap = currentPopulationMap;
////            return false;
////        }
////        boolean result = lastPopulationMap.equals(currentPopulationMap);
////        lastPopulationMap = currentPopulationMap;
////        return result;
//    }
    private double percentageDifference(List<Warrior> lastPopulation, List<Warrior> currentPopulation) {
        double totalSize = 2 * lastPopulation.size();
        HashMap<Warrior, Integer> lastPopulationMap     = new HashMap<>();
        HashMap<Warrior, Integer> currentPopulationMap  = new HashMap<>();

        WarriorUtils.loadMap(lastPopulationMap, lastPopulation);
        WarriorUtils.loadMap(currentPopulationMap, currentPopulation);
        double difference = 0;

        difference += getDifference(lastPopulationMap, currentPopulation);
        difference += getDifference(currentPopulationMap, lastPopulation);

        return  difference / totalSize;
    }

    private int getDifference(HashMap<Warrior, Integer> map, List<Warrior> population) {
        int difference = 0;
        for(Warrior w : population) {
            if(map.containsKey(w)) {
                Integer value = map.get(w);
                if(value == 0) {
                    difference ++;
                }
                else {
                    map.put(w, value - 1);
                }
            }
            else {
                difference++;
            }
        }

        return difference;
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
