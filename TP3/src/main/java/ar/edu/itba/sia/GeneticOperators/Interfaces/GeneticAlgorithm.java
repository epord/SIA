package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Genetic algorithm. Basically does the following:
 * <ol>
 *     <li>Select a number of fittest parents</li>
 *     <li>Cross them</li>
 *     <li>Mutate the children</li>
 *     <li>Select a combination of parents and children to pass on to the next generation</li>
 * </ol>
 */
public abstract class GeneticAlgorithm {

    protected final Selection selectionMethod;
    protected final CrossOver crossOverMethod;
    protected final double crossOverProbability;
    protected final Mutation mutationMethod;
    protected final double mutationProbability;
    protected final List<Item> helmets;
    protected final List<Item> platebodies;
    protected final List<Item> gloves;
    protected final List<Item> weapons;
    protected final List<Item> boots;
    protected final double minHeight, maxHeight;
    protected double generationGap;


    public GeneticAlgorithm(Selection selectionMethod,
                            CrossOver crossOverMethod, double crossOverProbability,
                            Mutation mutationMethod, double mutationProbability,
                            double generationGap,
                            List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots,
                            double minHeight, double maxHeight) {
        this.selectionMethod = selectionMethod;
        this.crossOverMethod = crossOverMethod;
        this.crossOverProbability = crossOverProbability;
        this.mutationMethod = mutationMethod;
        this.mutationProbability = mutationProbability;
        this.generationGap = generationGap;

        this.helmets = helmets;
        this.platebodies = platebodies;
        this.gloves = gloves;
        this.weapons = weapons;
        this.boots = boots;

        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    /**
     * Perform an evolutionary step.
     *
     * @param population     Population to evolve.
     * @return List of {@code k} individuals, evolved from the previous generation.
     */
    public abstract List<Warrior> evolve(List<Warrior> population);

    /**
     * Calculate the number of children to create for the next generation according to generation gap.
     *
     * @param entirePopulation Entire population of the current generation.
     * @return The number of children needed.
     */
    protected int numChildrenToCreate(List<Warrior> entirePopulation) {
        return (int) ((1 - generationGap) * entirePopulation.size());
    }

    /**
     * Calculate the number of individuals to pass directly to the next generation according to generation gap.
     *
     * @param entirePopulation Entire population of the current generation.
     * @return The number of individuals to pass directly to the next generation.
     */
    protected int numIndividualsToPassThrough(List<Warrior> entirePopulation) {
        return entirePopulation.size() - numChildrenToCreate(entirePopulation);
    }

    /**
     * Generate {@link #numChildrenToCreate(List)} children from the given population, using the configured selection method
     * to select the parents, and crossing them with the configured probability (if no cross, pass un-mutated parents
     * directly as children).
     *
     * @param population Population from which to generate children.
     * @return The generated children.
     */
    protected List<Warrior> generateChildren(List<Warrior> population) {
        int K = numChildrenToCreate(population);
        // Select K parents
        List <Warrior> parents = selectionMethod.select(population, K);
        // Generate K children
        List<Warrior> children = new ArrayList<>(K);
        for (int i = 0; i < K - 1; i++) {
            if (Math.random() <= crossOverProbability) {
                children.addAll(
                        mutate(crossOverMethod.apply(parents.get(i), parents.get(i+1)))
                );
            } else {
                children.add(parents.get(i));
                children.add(parents.get(i+1));
            }
        }
        return children;
    }

    /**
     * Mutate the given population with the configured mutation probability.
     *
     * @param population Population to potentially mutate.
     * @return The mutated population.
     */
    protected List<Warrior> mutate(List<Warrior> population) {
        return population.stream().map(individual -> {
            if(Math.random() <= mutationProbability) {
                return mutationMethod.mutate(individual, boots, gloves, platebodies, helmets, weapons, minHeight, maxHeight);
            }
            else {
                return individual;
            }
        }).collect(Collectors.toList());
    }
}
