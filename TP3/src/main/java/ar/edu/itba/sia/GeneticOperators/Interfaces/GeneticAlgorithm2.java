package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Items.Item;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

/**
 * Same as {@link GeneticAlgorithm} but also has configurable replacement methods.
 */
public abstract class GeneticAlgorithm2 extends GeneticAlgorithm {

    protected final double replacementSelectionMethodProportion;
    protected final Selection replacementSelectionMethod1, replacementSelectionMethod2;

    public GeneticAlgorithm2(double crossOverProbability, double crossOverSelectionProportion, Selection crossOverSelectionMethod1, Selection crossOverSelectionMethod2, CrossOver crossOverMethod, double mutationProbability, Mutation mutationMethod, double generationGap, double replacementSelectionMethodProportion, Selection replacementSelectionMethod1, Selection replacementSelectionMethod2, List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots, double minHeight, double maxHeight) {
        super(crossOverProbability, crossOverSelectionProportion, crossOverSelectionMethod1, crossOverSelectionMethod2, crossOverMethod, mutationProbability, mutationMethod, generationGap, helmets, platebodies, gloves, weapons, boots, minHeight, maxHeight);
        this.replacementSelectionMethodProportion = replacementSelectionMethodProportion;
        this.replacementSelectionMethod1 = replacementSelectionMethod1;
        this.replacementSelectionMethod2 = replacementSelectionMethod2;
    }

    /**
     * Select a proportion of individuals from the given population with {@link #replacementSelectionMethod1}, and the
     * remainder with {@link #replacementSelectionMethod2}. The proportion that goes with method 1 corresponds to
     * {@link #replacementSelectionMethodProportion}.
     *
     * @param population          Population from which to select.
     * @param individualsToSelect Total number of individuals to select.
     * @return The selected individuals, with the correct proportion chosen with each selection method.
     */
    protected List<Warrior> selectForReplacement(List<Warrior> population, int individualsToSelect) {
        int numMethod1 = (int) (replacementSelectionMethodProportion * individualsToSelect);
        List<Warrior> result = new ArrayList<>(individualsToSelect);
        result.addAll(replacementSelectionMethod1.select(population, numMethod1));
        result.addAll(replacementSelectionMethod2.select(population, individualsToSelect - numMethod1));

        return result;
    }
}
