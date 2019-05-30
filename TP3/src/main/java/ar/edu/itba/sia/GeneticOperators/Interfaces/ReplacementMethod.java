package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public interface ReplacementMethod {
    public List<Warrior> getNetGeneration(List<Warrior> population, int k);

}
