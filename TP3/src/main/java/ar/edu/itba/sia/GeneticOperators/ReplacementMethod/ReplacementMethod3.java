package ar.edu.itba.sia.GeneticOperators.ReplacementMethod;

import ar.edu.itba.sia.GeneticOperators.Interfaces.*;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

import static ar.edu.itba.sia.BestWarriorFinder.generateChildren;
import static ar.edu.itba.sia.BestWarriorFinder.mutatePopulation;

public class ReplacementMethod3 implements ReplacementMethod {
    private Selection selectionMethod;
    private Mutation mutationMethod;
    private CrossOver crossOverMethod;
    private Selection replacementMethod;

    public ReplacementMethod3(Selection selectionMethod, Mutation mutationMethod,
                              CrossOver crossOverMethod, Selection replacementMethod) {
        this.selectionMethod    = selectionMethod;
        this.mutationMethod     = mutationMethod;
        this.crossOverMethod    = crossOverMethod;
        this.replacementMethod  = replacementMethod;
    }

    public List<Warrior> getNetGeneration(List<Warrior> population, int k) {
        List <Warrior> generators;
        List <Warrior> nextGeneration;
        int populationNumber = population.size();
        // Select K fittest parents
        generators = selectionMethod.select(population, k);
        // Cross them (generate children)
        nextGeneration = generateChildren(crossOverMethod, generators, k);
        // Mutate children
        nextGeneration = mutatePopulation(mutationMethod, nextGeneration, 0.01);
        // Add previous generation
        nextGeneration.addAll(population);

        population = replacementMethod.select(population, populationNumber);;
        return population;
    }
}
