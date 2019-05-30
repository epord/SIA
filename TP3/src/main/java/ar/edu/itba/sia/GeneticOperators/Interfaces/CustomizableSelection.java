package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

/**
 * Selection method that allows using custom fitnesses (eg. for Ranking or Boltzmann).
 */
public interface CustomizableSelection extends Selection {

    @Override
    default List<Warrior> select(List<Warrior> warriors, int quantity) {
        return select(warriors, quantity, null);
    }

    /**
     * Select {@code quantity} warriors from the given warrior list according to a selection criterion.
     *
     * @param warriors        Warriors to select from.
     * @param quantity        Number of warriors to select.
     * @param customFitnesses (Optional) Custom fitnesses to use for selection. If null, will use {@link Warrior#getFitness()}.
     *                        If provided, should correspond to custom warrior fitnesses by index.
     * @return The selected warriors.
     */
    List<Warrior> select(List<Warrior> warriors, int quantity, List<Double> customFitnesses) throws IllegalArgumentException;
}
