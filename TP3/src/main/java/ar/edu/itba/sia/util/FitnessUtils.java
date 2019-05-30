package ar.edu.itba.sia.util;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public class FitnessUtils {

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
}
