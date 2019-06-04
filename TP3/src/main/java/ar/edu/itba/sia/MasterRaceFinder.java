package ar.edu.itba.sia;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;
import ar.edu.itba.sia.util.Constants;
import ar.edu.itba.sia.util.Settings;
import ar.edu.itba.sia.util.TSVReader;

import java.io.IOException;
import java.util.List;

import static ar.edu.itba.sia.Warriors.WarriorBuilder.buildWarrior;
import static ar.edu.itba.sia.util.Settings.getDouble;
import static ar.edu.itba.sia.util.Settings.getWarriorType;


public class MasterRaceFinder {

    private static List<Item> boots;
    private static List<Item> gloves;
    private static List<Item> platebodies;
    private static List<Item> helmets;
    private static List<Item> weapons;

    // Multipliers
    private static final double STRENGTH_MULTIPLIER = getDouble(Constants.STRENGTH_MULTIPLIER);
    private static final double AGILITY_MULTIPLIER = getDouble(Constants.AGILITY_MULTIPLIER);
    private static final double EXPERTISE_MULTIPLIER = getDouble(Constants.EXPERTISE_MULTIPLIER);
    private static final double RESISTANCE_MULTIPLIER = getDouble(Constants.RESISTANCE_MULTIPLIER);
    private static final double HEALTH_MULTIPLIER = getDouble(Constants.HEALTH_MULTIPLIER);

    public static void main(String[] args) throws IOException {
        Warrior masterRace = find(WarriorType.ARCHER);
        System.out.println(masterRace);
    }

    public static Warrior find(WarriorType warriorType) throws IOException {
        return find(warriorType,  null, null, null, null, null);
    }

    public static Warrior find(WarriorType warriorType, List<Item> helmets, List<Item> platebodies, List<Item> gloves, List<Item> weapons, List<Item> boots) throws IOException {
        MasterRaceFinder.helmets = helmets;
        MasterRaceFinder.platebodies = platebodies;
        MasterRaceFinder.gloves = gloves;
        MasterRaceFinder.weapons = weapons;
        MasterRaceFinder.boots = boots;

        if (helmets == null) {
            generateEquipment();
            // TODO menos cabeza pls
        }

        double bestHeight, ATM, DEM;

        // This is pre-calculated and depends on the functions ATM and DFM
        if (warriorType.getAttackFactor() > warriorType.getDefenseFactor()) {
            bestHeight = 1.91519;
            ATM = 1.70447;
            DEM = 0.79553;
        } else {
            bestHeight = 1.63849;
            ATM = 1.32634;
            DEM = 1.17366;
        }
        Boots bestBoots = (Boots) findBestItem(MasterRaceFinder.boots, warriorType, ATM, DEM);
        Gloves bestGloves = (Gloves) findBestItem(MasterRaceFinder.gloves, warriorType, ATM, DEM);
        Helmet bestHelmet = (Helmet) findBestItem(MasterRaceFinder.helmets, warriorType, ATM, DEM);
        Platebody bestPlatebody = (Platebody) findBestItem(MasterRaceFinder.platebodies, warriorType, ATM, DEM);
        Weapon bestWeapon = (Weapon) findBestItem(MasterRaceFinder.weapons, warriorType, ATM, DEM);

        return buildWarrior(bestBoots, bestGloves, bestPlatebody, bestHelmet, bestWeapon, bestHeight,
                            getWarriorType());
    }

    private static Item findBestItem(List<Item> items, WarriorType warriorType, double ATM, double DEM) {
        Item bestItem = null;
        double bestFitness = 0.0;
        for (Item item: items) {
            double strength = 100 * Math.tanh(0.01 * item.getStrength() * STRENGTH_MULTIPLIER);
            double agility = Math.tanh(0.01 * item.getAgility() * AGILITY_MULTIPLIER);
            double expertise = 0.6 * Math.tanh(0.01 * item.getExpertise() * EXPERTISE_MULTIPLIER);
            double resistance = Math.tanh(0.01 * item.getResistance() * RESISTANCE_MULTIPLIER);
            double hitPoints = 100 * Math.tanh(0.01 * item.getHitPoints() * HEALTH_MULTIPLIER);
            double attack = (agility + expertise) * strength * ATM;
            double defense = (resistance + expertise) * hitPoints * DEM;
            double fitness = warriorType.getAttackFactor() * attack + warriorType.getDefenseFactor() * defense;
            if (fitness > bestFitness || bestItem == null) {
                bestItem = item;
                bestFitness = fitness;
            }
        }
        return bestItem;
    }

    public static void generateEquipment() throws IOException {
        boolean readFullData = Settings.getBoolean(Constants.READ_FULL_DATA);

        boots = TSVReader.loadItems(ItemType.BOOTS, readFullData);
        gloves = TSVReader.loadItems(ItemType.GLOVES, readFullData);
        platebodies = TSVReader.loadItems(ItemType.PLATEBODY, readFullData);
        helmets = TSVReader.loadItems(ItemType.HELMET, readFullData);
        weapons = TSVReader.loadItems(ItemType.WEAPON, readFullData);
    }
}
