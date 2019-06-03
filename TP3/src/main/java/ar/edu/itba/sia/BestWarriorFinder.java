package ar.edu.itba.sia;

import ar.edu.itba.sia.GeneticOperators.Algorithms.Algorithm1;
import ar.edu.itba.sia.GeneticOperators.Algorithms.Algorithm2;
import ar.edu.itba.sia.GeneticOperators.Algorithms.Algorithm3;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.GeneticOperators.Interfaces.GeneticAlgorithm;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.GeneticOperators.Selections.EliteSelection;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;
import ar.edu.itba.sia.util.Constants;
import ar.edu.itba.sia.util.FileManager;
import ar.edu.itba.sia.util.MetricsGenerator;
import ar.edu.itba.sia.util.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.sia.util.Constants.*;
import static ar.edu.itba.sia.util.Settings.*;
import static ar.edu.itba.sia.util.TSVReader.loadItems;


public class BestWarriorFinder {
    private static final String DEFAULT_PROPERTIES_PATH = "settings.properties";

    private static double MIN_HEIGHT, MAX_HEIGHT;

    private static List<Item> Boots;
    private static List<Item> Gloves;
    private static List<Item> Platebodies;
    private static List<Item> Helmets;
    private static List<Item> Weapons;
    private static List<Warrior> population;
    private static GeneticAlgorithm algorithm;
    private static EndCondition endCondition;


    public static void main(String[] args) throws IOException {
        // Read configuration
        loadSettings(args.length == 0 ? DEFAULT_PROPERTIES_PATH : args[0]);
        MIN_HEIGHT = Settings.getDouble(Constants.MIN_HEIGHT);
        MAX_HEIGHT = Settings.getDouble(Constants.MAX_HEIGHT);
        endCondition = getEndCondition(getInt(END_CONDITION_TYPE));

        Warrior bestWarrior = findBestWarrior();
        System.out.println("Best warrior fitness: " + bestWarrior.getFitness());
    }

    public static Warrior findBestWarrior() throws IOException {
        generateEquipment();

        Warrior masterRaceWarrior = MasterRaceFinder.find(WarriorType.ARCHER, Helmets, Platebodies, Gloves, Weapons, Boots);
        System.out.println("Best fitness according to master race finder: " + masterRaceWarrior.getFitness() + "\n");

        loadGeneticOperators();
        int populationSize = getInt(Constants.POPULATION_SIZE);
        population = generatePopulation(populationSize, WarriorType.ARCHER);
        return findBestWarrior(population, algorithm, endCondition);
    }

    public static void generateEquipment() throws IOException {
        System.out.print("Reading items from file...");
        boolean readFullData = Settings.getBoolean(Constants.READ_FULL_DATA);

        Boots       = loadItems(ItemType.BOOTS, readFullData);
        Gloves      = loadItems(ItemType.GLOVES, readFullData);
        Platebodies = loadItems(ItemType.PLATEBODY, readFullData);
        Helmets     = loadItems(ItemType.HELMET, readFullData);
        Weapons     = loadItems(ItemType.WEAPON, readFullData);
        System.out.println("done");
    }

    public static void loadGeneticOperators() {

        algorithm = instanceChosenAlgorithm(getInt(ALGORITHM));
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
        double warriorHeight = Math.random() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT;

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
                                           EndCondition endCondition)  throws IOException{
        MetricsGenerator.addGeneration(population);
        int currGeneration = 0;

        while(!endCondition.test(population)) {
            population = geneticAlgorithm.evolve(population);
            MetricsGenerator.addGeneration(population);
            if (currGeneration % (getInt(MAX_GENERATIONS) / 10) == 0) {
                System.out.println("Current genreation: " + currGeneration);
                EliteSelection selector = new EliteSelection();
                System.out.println("Best warrior: " + selector.select(population, 1).get(0));
            }
            currGeneration++;
        }

        System.out.print("Calculating metrics...");
        // Write Octave output file
        FileManager fm = new FileManager();
        fm.writeStringToFile("out.m", MetricsGenerator.getOctaveCode());

        // Write visualization file
        fm.writeStringToFile("frontend/data.p5", MetricsGenerator.getVisualizationData());
        System.out.println("done");

        System.out.println("Total generations: " + currGeneration);

        EliteSelection selector = new EliteSelection();
        return selector.select(population, 1).get(0);
    }

    /**
     * Generate a fully configured instance of the specified algorithm.
     *
     * @param algorithmNumber Algorithm number. 1, 2 or 3.
     * @return An instance of the specified algorithm number, fully configured with all appropriate settings as read from
     * the settings file.
     */
    private static GeneticAlgorithm instanceChosenAlgorithm(int algorithmNumber) {
        Mutation mutationMethod = getMutationMethod(getInt(MUTATION_TYPE));
        CrossOver crossOverMethod = getCrossoverMethod(getInt(CROSSOVER_TYPE));

        switch (algorithmNumber) {
            case 1:
                return new Algorithm1(
                        // Crossover params
                        getDouble(Constants.CROSSOVER_PROBABILITY), getDouble(Constants.CROSSOVER_A), Settings.getSelectionMethod(1), Settings.getSelectionMethod(2), crossOverMethod,
                        // Mutation params
                        getDouble(Constants.MUTATION_PROBABILITY), mutationMethod,
                        // Available equipment
                        Helmets, Platebodies, Gloves, Weapons, Boots,
                        // Height restrictions
                        MIN_HEIGHT, MAX_HEIGHT);
            case 2:
                return new Algorithm2(
                        // Crossover params
                        getDouble(Constants.CROSSOVER_PROBABILITY), getDouble(Constants.CROSSOVER_A), Settings.getSelectionMethod(1), Settings.getSelectionMethod(2), crossOverMethod,
                        // Mutation params
                        getDouble(Constants.MUTATION_PROBABILITY), mutationMethod,
                        // Replacement params
                        getDouble(Constants.REPLACEMENT_GENERATION_GAP), getDouble(Constants.REPLACEMENT_B), Settings.getSelectionMethod(3), Settings.getSelectionMethod(4),
                        // Available equipment
                        Helmets, Platebodies, Gloves, Weapons, Boots,
                        // Height restrictions
                        MIN_HEIGHT, MAX_HEIGHT);
            case 3:
                return new Algorithm3(
                        // Crossover params
                        getDouble(Constants.CROSSOVER_PROBABILITY), getDouble(Constants.CROSSOVER_A), Settings.getSelectionMethod(1), Settings.getSelectionMethod(2), crossOverMethod,
                        // Mutation params
                        getDouble(Constants.MUTATION_PROBABILITY), mutationMethod,
                        // Replacement params
                        getDouble(Constants.REPLACEMENT_GENERATION_GAP), getDouble(Constants.REPLACEMENT_B), Settings.getSelectionMethod(3), Settings.getSelectionMethod(4),
                        // Available equipment
                        Helmets, Platebodies, Gloves, Weapons, Boots,
                        // Height restrictions
                        MIN_HEIGHT, MAX_HEIGHT);
            default:
                throw new IllegalArgumentException("Invalid algorithm number " + algorithmNumber + ". Admissible values are 1 through 3");
        }
    }
}
