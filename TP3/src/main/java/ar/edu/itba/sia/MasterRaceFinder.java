package ar.edu.itba.sia;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;
import ar.edu.itba.sia.util.TSVReader;

import java.io.IOException;
import java.util.List;


public class MasterRaceFinder {

    private static List<Item> boots;
    private static List<Item> gloves;
    private static List<Item> platebodies;
    private static List<Item> helmets;
    private static List<Item> weapons;

    public static void main(String[] args) throws IOException {
        Warrior masterRace = find(WarriorType.ARCHER);
        System.out.println(masterRace);
    }

    public static Warrior find(WarriorType warriorType) throws IOException {
        generateEquipment();
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
        Boots bestBoots = (Boots) findBestItem(boots, warriorType, ATM, DEM);
        Gloves bestGloves = (Gloves) findBestItem(gloves, warriorType, ATM, DEM);
        Helmet bestHelmet = (Helmet) findBestItem(helmets, warriorType, ATM, DEM);
        Platebody bestPlatebody = (Platebody) findBestItem(platebodies, warriorType, ATM, DEM);
        Weapon bestWeapon = (Weapon) findBestItem(weapons, warriorType, ATM, DEM);

        /// TODO: return appropiate class
        return new Archer(bestBoots, bestGloves, bestPlatebody, bestHelmet, bestWeapon, bestHeight);
    }

    private static Item findBestItem(List<Item> items, WarriorType warriorType, double ATM, double DEM) {
        Item bestItem = null;
        double bestPerformance = 0.0;
        for (Item item: items) {
            double attack = (item.getAgility() + item.getExpertise()) * item.getStrength() * ATM;
            double defense = (item.getResistance() + item.getExpertise()) * item.getHitPoints() * DEM;
            double performance = warriorType.getAttackFactor() * attack + warriorType.getDefenseFactor() * defense;
            if (performance > bestPerformance || bestItem == null) {
                bestItem = item;
                bestPerformance = performance;
            }
        }
        return bestItem;
    }



    public static void generateEquipment() throws IOException {
        boots = TSVReader.loadItems(ItemType.BOOTS);
        gloves = TSVReader.loadItems(ItemType.GLOVES);
        platebodies = TSVReader.loadItems(ItemType.PLATEBODY);
        helmets = TSVReader.loadItems(ItemType.HELMET);
        weapons = TSVReader.loadItems(ItemType.WEAPON);
    }

}
