package ar.edu.itba.sia.GeneticOperators.Interfaces;


import ar.edu.itba.sia.Warriors.Warrior;

public interface CrossOver {
    Warrior getCrossOver(Warrior w1, Warrior w2);
}
