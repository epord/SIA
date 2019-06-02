package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
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

    protected final double crossOverProbability;
    protected final double crossOverSelectionMethodProportion;
    protected final Selection crossOverSelectionMethod1, crossOverSelectionMethod2;
    protected final CrossOver crossOverMethod;
    protected final Mutation mutationMethod;
    protected final double mutationProbability;
    protected final List<Item> helmets;
    protected final List<Item> platebodies;
    protected final List<Item> gloves;
    protected final List<Item> weapons;
    protected final List<Item> boots;
    protected final double minHeight, maxHeight;
    protected double generationGap;


    public GeneticAlgorithm(double crossOverProbability, double crossOverSelectionProportion, Selection crossOverSelectionMethod1,
                            Selection crossOverSelectionMethod2, CrossOver crossOverMethod,
                            double mutationProbability, Mutation mutationMethod,
                            double generationGap,
                            List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots,
                            double minHeight, double maxHeight) {
        this.crossOverProbability = crossOverProbability;
        this.crossOverSelectionMethodProportion = crossOverSelectionProportion;
        this.crossOverSelectionMethod1 = crossOverSelectionMethod1;
        this.crossOverSelectionMethod2 = crossOverSelectionMethod2;
        this.crossOverMethod = crossOverMethod;
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
     * @param population Entire population
     * @return Number of children to create with selection method 1.
     */
    protected int numChildrenToCreateWithMethod1(List<Warrior> population) {
        return (int) (crossOverSelectionMethodProportion * numChildrenToCreate(population));
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
        int method1NumChildren = numChildrenToCreateWithMethod1(population);

        // Select A*K parents with method 1
        List <Warrior> parents = crossOverSelectionMethod1.select(population, method1NumChildren);
        // Select (1-A)*K parents with method 2
        parents.addAll(crossOverSelectionMethod2.select(population, K-method1NumChildren));

        // Generate K children
        List<Warrior> children = new ArrayList<>(K);
        for (int i = 0; i < K; i++) {
            Warrior w1 = parents.get((int) (Math.random() * parents.size()));
            Warrior w2 = parents.get((int) (Math.random() * parents.size()));
            if (Math.random() <= crossOverProbability) {
                // Cross parents, mutate children, choose only one
                children.add(
                        mutate(crossOverMethod.apply(w1, w2)).get((int) (Math.random() * 2))
                );
            } else {
                children.add(Math.random() <= 0.5 ? w1 : w2);
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
