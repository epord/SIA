package ar.edu.itba.sia.GeneticOperators.Selections.Boltzmann;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CustomizableSelection;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoltzmannSelection implements Selection {
    private final CustomizableSelection secondSelection;
    private int currentGeneration = 0;
    private Function<Integer, Double> tempFunction;

    /**
     * @param secondSelection Customizable selection method to use with new fitnesses after ranking.
     */
    public BoltzmannSelection(CustomizableSelection secondSelection, Function<Integer, Double> tempFunction) {
        this.secondSelection = secondSelection;
        this.tempFunction = tempFunction;
    }

    @Override
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        List<Double> customFitnesses = new ArrayList<>(quantity);
        double temperature = tempFunction.apply(currentGeneration);
        double accumulatedExp = 0;
        for(int i = 0; i < quantity; i++) {
            double currentExp = Math.exp(warriors.get(i).getFitness() / temperature);
            accumulatedExp += currentExp;
            customFitnesses.add(currentExp);
        }
        double meanExp = accumulatedExp / quantity;
        customFitnesses = customFitnesses.stream().map(c -> c/meanExp).collect(Collectors.toList());

        return secondSelection.select(new ArrayList<>(warriors.subList(0, quantity)), quantity, customFitnesses);
    }

    /**
     * To be called when the population advances one generation, so as to update temperature accordingly.
     */
    public void onGenerationUpdated() {
        currentGeneration++;
    }
}
