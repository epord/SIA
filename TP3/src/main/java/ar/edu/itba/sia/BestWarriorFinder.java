package ar.edu.itba.sia;

import ar.edu.itba.sia.GeneticOperators.Crossovers.OnePointCrossover;
import ar.edu.itba.sia.GeneticOperators.EndConditions.MaxGenerationsEndCondition;
import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.GeneticOperators.Selections.EliteSelection;
import ar.edu.itba.sia.GeneticOperators.Mutations.GenMutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;

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
    public static List<Warrior> children;


    public static void main(String[] args) throws IOException{
       //loadSettings("TP3/src/main/settings.properties");
        Warrior bestWarrior = findBestWarrior();
        System.out.println("Best warrior performance: " + bestWarrior.getPerformance() );
    }

    public static Warrior findBestWarrior() throws IOException {
        generateEquipment();
        int populationSize = 10; //should be read from properties TODO
        population = generatePopulation(populationSize, WarriorType.ARCHER);
        int maxGenerations = 10000;
        return findBestWarrior(population, new EliteSelection(), new GenMutation(),
                                                new OnePointCrossover(), new EliteSelection(),
                                                maxGenerations, 5, populationSize,
                                                new MaxGenerationsEndCondition(maxGenerations));
    }

    public static void generateEquipment() throws IOException {
        Boots       = loadItems(ItemType.BOOTS);
        Gloves      = loadItems(ItemType.GLOVES);
        Platebodies = loadItems(ItemType.PLATEBODY);
        Helmets     = loadItems(ItemType.HELMET);
        Weapons     = loadItems(ItemType.WEAPON);
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
                throw new IllegalArgumentException("");
        }
        return null;
    }

    private static Warrior findBestWarrior(List<Warrior> population, Selection selectionMethod,
                                           Mutation mutationMethod, CrossOver crossOverMethod,
                                           Selection replacementMethod, int maxGeneration, int k,
                                           int populationNumber, EndCondition endCondition) {
        int currGeneration = 0;
        List <Warrior> generators;
        List <Warrior> nextGeneration;

        // Metodo de reemplazo 3 TODO implementar los otros dos y poder elegir
        while(!endCondition.test(population)) {
            generators = selectionMethod.select(population, k);
            nextGeneration = generateChildren(crossOverMethod, generators, k);
            nextGeneration.addAll(population);
            // FIXME mutar sólo hijos antes de agregar el resto de la población
            nextGeneration = mutatePopulation(mutationMethod, nextGeneration, 0.01);
            population = replacePopulation(replacementMethod, nextGeneration, populationNumber);
            currGeneration ++;
        }
        EliteSelection selector = new EliteSelection();
        return selector.select(population, 1).get(0);
    }

    private  static List<Warrior> generateChildren(CrossOver crossOverMethod, List<Warrior> generators, int k) {
        List<Warrior> children = new ArrayList<>();
        for(int i = 0; i < k; i++) {
            Warrior w1 = generators.get((int) (Math.random() * generators.size()));
            Warrior w2 = generators.get((int) (Math.random() * generators.size()));
            children.addAll(crossOverMethod.getCrossOver(w1, w2));
        }
        return children;
    }

    private static List<Warrior> mutatePopulation(Mutation mutationMethod, List<Warrior> population,
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

    private static List<Warrior> replacePopulation(Selection replacementMethod, List<Warrior> population,
                                                   int populationNumber) {
        return replacementMethod.select(population, populationNumber);
    }
}
