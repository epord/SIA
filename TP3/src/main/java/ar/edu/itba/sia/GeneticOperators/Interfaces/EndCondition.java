package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;
import java.util.function.Predicate;

public interface EndCondition extends Predicate<List<Warrior>> {

    @Override
    boolean test(List<Warrior> population);
}
