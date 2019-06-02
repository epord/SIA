package ar.edu.itba.sia.GeneticOperators.Algorithms;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.GeneticOperators.Interfaces.GeneticAlgorithm;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Items.Item;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class Algorithm3 extends GeneticAlgorithm {

    private final Selection replacementSelectionMethod;

    public Algorithm3(Selection selectionMethod1, CrossOver crossOverMethod, double crossOverProbability, Mutation mutationMethod, double mutationProbability, double generationGap, Selection selectionMethod2, List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots, double minHeight, double maxHeight) {
        super(selectionMethod1, crossOverMethod, crossOverProbability, mutationMethod, mutationProbability, generationGap, helmets, platebodies, gloves, weapons, boots, minHeight, maxHeight);
        this.replacementSelectionMethod = selectionMethod2;
    }

    public List<Warrior> evolve(List<Warrior> population) {
        int N = population.size();
        List <Warrior> newGeneration = new ArrayList<>(N);
        List<Warrior> children = generateChildren(population);

        // Select and pass numIndividuals - K individuals from previous generation directly to new generation
        int k = children.size();
        newGeneration.addAll(replacementSelectionMethod.select(population, N - k));
        // Select K individuals from a group containing both old and new populations to pass to new generation
        List<Warrior> oldPlusNew = new ArrayList<>(population);
        oldPlusNew.addAll(children);
        newGeneration.addAll(replacementSelectionMethod.select(oldPlusNew, k));

        return newGeneration;
    }

//    public List<Warrior> originalEvolve(List<Warrior> population, int numIndividuals) {
//        List <Warrior> generators;
//        List <Warrior> nextGeneration;
//        int populationNumber = population.size();
//        // Select K fittest parents
//        generators = selectionMethod.select(population, numIndividuals);
//        // Cross them (generate children)
//        nextGeneration = generateChildren(crossOverMethod, generators, numIndividuals);
//        // Mutate children
//        nextGeneration = mutatePopulation(mutationMethod, nextGeneration, 0.01);
//        // Add previous generation
//        nextGeneration.addAll(population);
//
//        population = replacementMethod.select(population, populationNumber);;
//        return population;
//    }
}
