package ar.edu.itba.sia.GeneticOperators.Crossovers;

import ar.edu.itba.sia.GeneticOperators.Genes;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public class TwoPointsCrossOver implements CrossOver {


    public List<Warrior> getCrossOver(Warrior w1, Warrior w2) {
        Genes[] genes = Genes.values();
        int locus1 = (int) (Math.random() * genes.length);
        int locus2 = (int) (Math.random() * genes.length);

        Annular annular = new Annular();
        return annular.getCrossOver(w1, w2, locus1, locus2 - locus1 + 1);

    }
}