package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;
import java.util.List;

public interface Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity);
}