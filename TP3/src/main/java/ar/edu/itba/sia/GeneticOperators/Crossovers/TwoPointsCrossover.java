package ar.edu.itba.sia.GeneticOperators.Crossovers;

import ar.edu.itba.sia.GeneticOperators.Genes;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public class TwoPointsCrossover implements CrossOver {


    public List<Warrior> apply(Warrior w1, Warrior w2) {
        Genes[] genes = Genes.values();
        int locus1 = (int) (Math.random() * genes.length);
        int locus2 = (int) (Math.random() * genes.length);

        // Perform an annular crossover from min locus to max locus, which will NOT wrap around
        return new AnnularCrossover().getCrossOver(w1, w2, Math.min(locus1, locus2), Math.abs(locus2 - locus1) + 1);
    }
}
