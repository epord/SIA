package ar.edu.itba.sia.GeneticOperators.Algorithms;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.GeneticOperators.Interfaces.GeneticAlgorithm;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Items.Item;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class Algorithm1 extends GeneticAlgorithm {

    public Algorithm1(Selection selectionMethod, CrossOver crossOverMethod, double crossOverProbability, Mutation mutationMethod, double mutationProbability, List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots, double minHeight, double maxHeight) {
        super(selectionMethod, crossOverMethod, crossOverProbability, mutationMethod, mutationProbability, 1, helmets, platebodies, gloves, weapons, boots, minHeight, maxHeight);
    }

    public List<Warrior> evolve(List<Warrior> population) {
        List <Warrior> parents = new ArrayList<>(population);
        int N = population.size();
        List <Warrior> newGeneration = new ArrayList<>(N);
        while (newGeneration.size() < N) {
            // Select 2 fittest parents
            List<Warrior> fittestParents = selectionMethod.select(parents, 2);
            //parents.removeAll(fittestParents);

            // Potentially generateChildren them (generate children), otherwise pass them directly
            List<Warrior> children = Math.random() <= crossOverProbability
                    ? crossOverMethod.apply(fittestParents.get(0), fittestParents.get(1))
                    : fittestParents;
            // Potentially mutate
            children = mutate(children);

            newGeneration.addAll(children);
        }
        return newGeneration;
    }
}
