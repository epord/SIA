package ar.edu.itba.sia;

import ar.edu.itba.sia.GeneticOperators.Crossovers.OnePointCrossover;
import ar.edu.itba.sia.GeneticOperators.Crossovers.UniformCrossover;
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
        generateEquipment();
        int poblationNumber = 10; //should be read from properties TODO
        population = generatePopulation(poblationNumber, WarriorType.ARCHER);
        int maxGenerations = 10000;
        Warrior bestWarrior = findBestWarrior(population, new EliteSelection(), new GenMutation(),
                                                new OnePointCrossover(), new EliteSelection(),
                                                maxGenerations, 5, poblationNumber);
//        System.out.println(poblation.size());
//
//        Boots leatherBoots         = new Boots(10,10,10,10,10);
//        Gloves leatherGloves      = new Gloves(10,10,10,10,10);
//        Platebody cooperPlatebody = new Platebody(10,10,10,10,10);
//        Helmet cooperHelmet = new Helmet(10,10,10,10,10);
//        Weapon cooperBow = new Weapon(10,10,10,10,10);
//
//        Archer archer = new Archer(leatherBoots, leatherGloves, cooperPlatebody, cooperHelmet, cooperBow, 1.65);
       // System.out.println("archers performance:" + archer.getPerformance());
        System.out.println( "bestWarrior performance:" + bestWarrior.getPerformance() );
    }

    public static void generateEquipment() throws IOException {
        Boots       = loadItems(ItemType.BOOTS);
        Gloves      = loadItems(ItemType.GLOVES);
        Platebodies = loadItems(ItemType.PLATEBODY);
        Helmets     = loadItems(ItemType.HELMET);
        Weapons     = loadItems(ItemType.WEAPON);
    }

    public static List<Warrior> generatePopulation(int populationNumber, WarriorType warriorType) {
        List<Warrior> warriors = new ArrayList<>();
        for(int i = 0; i < populationNumber; i++) {
            warriors.add(generateRandomWarrior(warriorType));
        }
        return warriors;

    }

    private static Warrior generateRandomWarrior(WarriorType warriorType) {
        ar.edu.itba.sia.Items.Boots warriorBoots = (ar.edu.itba.sia.Items.Boots)Boots.get((int)(Math.floor(Math.random() * Boots.size())));
        Gloves warriorGloves = (ar.edu.itba.sia.Items.Gloves)Gloves.get((int)(Math.floor(Math.random() * Gloves.size())));
        Platebody warriorPlatebody = (Platebody) Platebodies.get((int)(Math.floor(Math.random() * Platebodies.size())));
        Helmet warriorHelmet = (Helmet) Helmets.get((int)(Math.floor(Math.random() * Helmets.size())));
        Weapon warriorWeapon = (Weapon) Weapons.get((int)(Math.floor(Math.random() * Weapons.size())));
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
                                           int populationNumber) {
        int currGeneration = 0;
        List <Warrior> generators;
        List <Warrior> nextGeneration;


        while(currGeneration < maxGeneration) {
            generators = selectionMethod.select(population, k);
            nextGeneration = generateChildren(crossOverMethod, generators, k);
            nextGeneration.addAll(population);
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
            Warrior w1 = generators.get((int)Math.random() * generators.size());
            Warrior w2 = generators.get((int)Math.random() * generators.size());
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
