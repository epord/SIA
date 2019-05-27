package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;
import java.util.function.BiFunction;

public interface CrossOver extends BiFunction<Warrior, Warrior, List<Warrior>> {

    @Override
    public List<Warrior> apply(Warrior w1, Warrior w2);
}
