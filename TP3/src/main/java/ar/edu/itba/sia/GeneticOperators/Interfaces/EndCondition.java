package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public interface EndCondition {
    boolean evaluate(List<Warrior> population);
}
