package ar.edu.itba.sia;

import ar.edu.itba.sia.GeneticOperators.EliteSelection;
import ar.edu.itba.sia.GeneticOperators.GenMutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.GeneticOperators.OnePointCrossOver;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.itba.sia.util.Properties.loadSettings;
import static ar.edu.itba.sia.util.TSVReader.loadItems;

/**
 * Hello world!
 *
 */
public class BestWarriorFinder {
    private static final double MINHEIGHT = 1.3; //should be read from properties TODO
    private static final double MAXHEIGHT = 2.0; //should be read from properties TODO
    private static List<Item> Boots;
    private static List<Item> Gloves;
    private static List<Item> Platebodies;
    private static List<Item> Helmets;
    private static List<Item> Weapons;
    private static List<Warrior> poblation;
    public static List<Warrior> children;


    public static void main( String[] args ) throws IOException{

       //loadSettings("TP3/src/main/settings.properties");
        generateEquipment();
        int poblationNumber = 10; //should be read from properties TODO
        poblation = generatePoblation(poblationNumber, WarriorType.ARCHER);
        int maxGenerations = 10000;
        Warrior bestWarrior = findBestWarrior(poblation, new EliteSelection(), new GenMutation(),
                                                new OnePointCrossOver(), new EliteSelection(),
                                                maxGenerations, 5, poblationNumber);
//        System.out.println(poblation.size());
//
//        Boot leatherBoots         = new Boot(10,10,10,10,10);
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
        Boots       = loadItems(ItemType.BOOT);
        Gloves      = loadItems(ItemType.GLOVES);
        Platebodies = loadItems(ItemType.PLATEBODY);
        Helmets     = loadItems(ItemType.HELMET);
        Weapons     = loadItems(ItemType.WEAPON);
    }

    public static List<Warrior> generatePoblation(int poblationNumber, WarriorType warriorType) {
        List<Warrior> warriors = new ArrayList<>();
        for(int i = 0; i < poblationNumber; i++) {
            warriors.add(generateRandomWarrior(warriorType));
        }
        return warriors;

    }

    private static Warrior generateRandomWarrior(WarriorType warriorType) {
        Boot warriorBoots = (Boot)Boots.get((int)(Math.floor(Math.random() * Boots.size())));
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

    private static Warrior findBestWarrior(List<Warrior> poblation, Selection selectionMethod,
                                           Mutation mutationMethod, CrossOver crossOverMethod,
                                           Selection replacementMethod, int maxGeneration, int k,
                                           int poblationNumber) {
        int currGeneration = 0;
        List <Warrior> generators = new ArrayList<>();
        List <Warrior> nextGeneration = new ArrayList<>();


        while(currGeneration < maxGeneration) {
            generators = selectionMethod.select(poblation, k);
            nextGeneration = generateChildren(crossOverMethod, generators, k);
            nextGeneration.addAll(poblation);
            nextGeneration = mutatePoblation(mutationMethod, nextGeneration, 0.5);
            poblation = replacePoblation(replacementMethod, nextGeneration, poblationNumber);
            currGeneration ++;
        }
        EliteSelection selector = new EliteSelection();
        return selector.select(poblation, 1).get(0);
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

    private static List<Warrior> mutatePoblation(Mutation mutationMethod, List<Warrior> poblation,
                                                 double mutationPercentage) {
        List<Warrior> newPoblation = new ArrayList<>();

        for(Warrior w : poblation) {
            if(Math.random() >= mutationPercentage) {
                newPoblation.add(mutationMethod.mutate(w, Boots, Gloves, Platebodies, Helmets,
                                                        Weapons, MINHEIGHT, MAXHEIGHT));
            }
            else {
                newPoblation.add(w);
            }
        }

        return newPoblation;
    }

    private static List<Warrior> replacePoblation(Selection replacementMethod, List<Warrior> poblation,
                                                  int poblationNumber) {
        return replacementMethod.select(poblation, poblationNumber);
    }
}
