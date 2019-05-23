package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public interface CrossOver {
    public List<Warrior> getCrossOver(Warrior w1, Warrior w2);
}
