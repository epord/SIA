package ar.edu.itba.sia.GeneticOperators.Algorithms;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.GeneticOperators.Interfaces.GeneticAlgorithm;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.GeneticOperators.Mutations.GeneralMutation;
import ar.edu.itba.sia.Items.Item;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class Algorithm1 extends GeneticAlgorithm {

    public Algorithm1(double crossOverProbability, double crossOverSelectionProportion, Selection crossOverSelectionMethod1, Selection crossOverSelectionMethod2, CrossOver crossOverMethod, double mutationProbability, Mutation mutationMethod, List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots, double minHeight, double maxHeight) {
        super(crossOverProbability, crossOverSelectionProportion, crossOverSelectionMethod1, crossOverSelectionMethod2, crossOverMethod, mutationProbability, mutationMethod, 1, helmets, platebodies, gloves, weapons, boots, minHeight, maxHeight);
    }

    public List<Warrior> evolve(List<Warrior> population) {
        List <Warrior> parents = new ArrayList<>(population);
        int N = population.size();
        List <Warrior> newGeneration = new ArrayList<>(N);
        while (newGeneration.size() < N) {
            // Select 2 fittest parents with appropriate selection method
            Selection selectionMethod = newGeneration.size() <= numChildrenToCreateWithMethod1(population)
                    ? crossOverSelectionMethod1
                    : crossOverSelectionMethod2;
            List<Warrior> fittestParents = selectionMethod.select(parents, 2);

            // Potentially cross them (generate children), otherwise pass them directly
            List<Warrior> children = Math.random() <= crossOverProbability
                    ? crossOverMethod.apply(fittestParents.get(0), fittestParents.get(1))
                    : fittestParents;
            // Potentially mutate
            children = mutate(children);

            newGeneration.addAll(children);
        }
        updateTemperature();

        if(!GeneralMutation.isUniform()) {
            GeneralMutation.modifyProbability();
        }

        return newGeneration;
    }
}
