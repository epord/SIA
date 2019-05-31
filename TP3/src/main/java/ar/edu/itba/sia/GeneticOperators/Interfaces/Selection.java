package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;
import java.util.List;

public interface Selection {

    /**
     * Select {@code quantity} warriors from the given warrior list according to a selection criterion.
     *
     * @param warriors        Warriors to select from.
     * @param quantity        Number of warriors to select.
     * @return The selected warriors.
     */
    List<Warrior> select(List<Warrior> warriors, int quantity);
}
