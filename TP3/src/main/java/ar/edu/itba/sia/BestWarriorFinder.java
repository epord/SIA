package ar.edu.itba.sia;

import ar.edu.itba.sia.GeneticOperators.Algorithms.Algorithm2;
import ar.edu.itba.sia.GeneticOperators.Crossovers.OnePointCrossover;
import ar.edu.itba.sia.GeneticOperators.EndConditions.*;
import ar.edu.itba.sia.GeneticOperators.Interfaces.*;
import ar.edu.itba.sia.GeneticOperators.Mutations.SingleGeneMutation;
import ar.edu.itba.sia.GeneticOperators.Selections.EliteSelection;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;
import ar.edu.itba.sia.util.MetricsGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.sia.util.TSVReader.loadItems;


public class BestWarriorFinder {
    private static final double MINHEIGHT = 1.3; //should be read from properties TODO
    private static final double MAXHEIGHT = 2.0; //should be read from properties TODO
    private static List<Item> Boots;
    private static List<Item> Gloves;
    private static List<Item> Platebodies;
    private static List<Item> Helmets;
    private static List<Item> Weapons;
    private static List<Warrior> population;
    private static List<Warrior> children;
    private static Selection selectionMethod;
    private static Mutation mutationMethod;
    private static CrossOver crossOverMethod;
    private static Selection replacementSelection;
    private static EndCondition endCondition;
    private static GeneticAlgorithm algorithm;


    public static void main(String[] args) throws IOException{
       //loadSettings("TP3/src/main/settings.properties");
        Warrior bestWarrior = findBestWarrior();
        System.out.println("Best warrior fitness: " + bestWarrior.getFitness());
    }

    public static Warrior findBestWarrior() throws IOException {
        generateEquipment();
        loadGeneticOperators();
        int populationSize = 50; //should be read from properties TODO
        population = generatePopulation(populationSize, WarriorType.ARCHER);
        return findBestWarrior(population, algorithm, endCondition);
    }

    public static void generateEquipment() throws IOException {
        Boots       = loadItems(ItemType.BOOTS);
        Gloves      = loadItems(ItemType.GLOVES);
        Platebodies = loadItems(ItemType.PLATEBODY);
        Helmets     = loadItems(ItemType.HELMET);
        Weapons     = loadItems(ItemType.WEAPON);
    }

    public static void loadGeneticOperators() throws IOException {
        int maxGenerations = 10000;
        int maxConsecutiveGenerations = 50;
        Warrior masterRaceWarrior = MasterRaceFinder.find(WarriorType.ARCHER);
        double nearOptimalError = 0.05;
        double NonChangingPopulationPercentage = 0.05;
        //TODO everything should be read from properties
        selectionMethod         = new EliteSelection();
        mutationMethod          = new SingleGeneMutation();
        crossOverMethod         = new OnePointCrossover();
        replacementSelection    = new EliteSelection();
        endCondition            = new EndConditionsCombiner(
                                        new MaxGenerationsEndCondition(maxGenerations)
                                        , new ContentEndCondition(maxConsecutiveGenerations)
                                        , new NearOptimalEndCondition(masterRaceWarrior.getFitness(), nearOptimalError)
                                        , new StructuralEndCondition(NonChangingPopulationPercentage)
                                    );
        algorithm = new Algorithm2(selectionMethod, crossOverMethod, 1d, mutationMethod, 0.03, 0.75, replacementSelection,
                Helmets, Platebodies, Gloves, Weapons, Boots, MINHEIGHT, MAXHEIGHT);
    }

    public static List<Warrior> generatePopulation(int populationNumber, WarriorType warriorType) {
        return Stream
                .generate(() -> generateRandomWarrior(warriorType))
                .limit(populationNumber)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Warrior generateRandomWarrior(WarriorType warriorType) {
        Boots warriorBoots = (Boots) Boots.get((int) (Math.random() * Boots.size()));
        Gloves warriorGloves = (Gloves) Gloves.get((int) (Math.random() * Gloves.size()));
        Platebody warriorPlatebody = (Platebody) Platebodies.get((int) (Math.random() * Platebodies.size()));
        Helmet warriorHelmet = (Helmet) Helmets.get((int) (Math.random() * Helmets.size()));
        Weapon warriorWeapon = (Weapon) Weapons.get((int) (Math.random() * Weapons.size()));
        double warriorHeight = Math.random() * (MAXHEIGHT - MINHEIGHT) + MINHEIGHT;

        switch (warriorType) {
            case ARCHER:
                return new Archer(warriorBoots, warriorGloves, warriorPlatebody, warriorHelmet, warriorWeapon, warriorHeight);
            case SOLDIER:
            case DEFENSOR:
            case ASSASIN:
                throw new UnsupportedOperationException(warriorType.name() + " not implemented yet");
        }
        return null;
    }

    private static Warrior findBestWarrior(List<Warrior> population, GeneticAlgorithm geneticAlgorithm,
                                           EndCondition endCondition) {
        int currGeneration = 0;
        List <Warrior> generators;
        List <Warrior> nextGeneration;
        MetricsGenerator.addGeneration(population);

        while(!endCondition.test(population)) {
            population = geneticAlgorithm.evolve(population);
            MetricsGenerator.addGeneration(population);
            currGeneration++;
        }

        System.out.println(MetricsGenerator.getOctaveCode());
        EliteSelection selector = new EliteSelection();
        return selector.select(population, 1).get(0);
    }

//    private static Warrior findBestWarrior(List<Warrior> population, Selection selectionMethod,
//                                           Mutation mutationMethod, CrossOver crossOverMethod,
//                                           Selection algorithm, int maxGeneration, int k,
//                                           int populationNumber, EndCondition endCondition) {
//        int currGeneration = 0;
//        List <Warrior> generators;
//        List <Warrior> nextGeneration;
//
//        // Metodo de reemplazo 3 TODO implementar los otros dos y poder elegir
//        while(!endCondition.test(population)) {
//            // Select K fittest parents
//            generators = selectionMethod.select(population, k);
//            // Cross them (generate children)
//            nextGeneration = generateChildren(crossOverMethod, generators, k);
//            // Mutate children
//            nextGeneration = mutatePopulation(mutationMethod, nextGeneration, 0.01);
//            // Add previous generation
//            nextGeneration.addAll(population);
//
//            population = replacePopulation(algorithm, nextGeneration, populationNumber);
//            currGeneration++;
//        }
//        EliteSelection selector = new EliteSelection();
//        return selector.select(population, 1).get(0);
//    }

    public  static List<Warrior> generateChildren(CrossOver crossOverMethod, List<Warrior> parents, int numChildren) {
        List<Warrior> children = new ArrayList<>();
        for(int i = 0; i < numChildren; i++) {
            Warrior w1 = parents.get((int) (Math.random() * parents.size()));
            Warrior w2 = parents.get((int) (Math.random() * parents.size()));
            children.addAll(crossOverMethod.apply(w1, w2));
        }
        return children;
    }

    public static List<Warrior> mutatePopulation(Mutation mutationMethod, List<Warrior> population,
                                                  double mutationPercentage) {
        List<Warrior> newPopulation = new ArrayList<>();

        for(Warrior w : population) {
            if(Math.random() <= mutationPercentage) {
                newPopulation.add(mutationMethod.mutate(w, Boots, Gloves, Platebodies, Helmets,
                                                        Weapons, MINHEIGHT, MAXHEIGHT));

            }
            else {
                newPopulation.add(w);
            }
        }

        return newPopulation;
    }

    public static List<Warrior> replacePopulation(Selection replacementMethod, List<Warrior> population,
                                                   int populationNumber) {
        return replacementMethod.select(population, populationNumber);
    }
}
