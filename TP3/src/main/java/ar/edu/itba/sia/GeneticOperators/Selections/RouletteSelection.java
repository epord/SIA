package ar.edu.itba.sia.GeneticOperators.Selections;

import java.util.Arrays;

public class RouletteSelection extends UniversalSelection {

    @Override
    protected void initializeRandoms(double[] randoms, int quantity, double r) {
        super.initializeRandoms(randoms, quantity, r);
        // Sort randoms and behavior is exactly the same as universal selection
        Arrays.sort(randoms);
    }
}
