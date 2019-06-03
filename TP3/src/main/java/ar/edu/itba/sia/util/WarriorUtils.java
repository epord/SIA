package ar.edu.itba.sia.util;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.HashMap;
import java.util.List;

public class WarriorUtils {

    /**
     * Get the sum of fitnesses in the specified warrior list.
     */
    public static double getTotalFitness(List<Warrior> warriors) {
        return warriors.stream()
                .mapToDouble(Warrior::getFitness)
                .sum();
    }

    public static double getTotalCustomFitness(List<Double> customFitnesses) {
        return customFitnesses.stream().mapToDouble(f -> f).sum();
    }


    public static void loadMap(HashMap<Warrior, Integer> map, List<Warrior> population) {
        for(Warrior w : population) {
            if(map.containsKey(w)) {
                Integer value = map.get(w);
                map.put(w, value + 1);
            }
            else {
                map.put(w, 1);
            }
        }
    }
}
