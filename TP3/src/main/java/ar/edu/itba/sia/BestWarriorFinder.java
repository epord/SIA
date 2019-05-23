package ar.edu.itba.sia;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
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
        poblation = generatePoblation(poblationNumber, 0);
        System.out.println(poblation.size());

        Boot leatherBoots         = new Boot(10,10,10,10,10);
        Gloves leatherGloves      = new Gloves(10,10,10,10,10);
        Platebody cooperPlatebody = new Platebody(10,10,10,10,10);
        Helmet cooperHelmet = new Helmet(10,10,10,10,10);
        Weapon cooperBow = new Weapon(10,10,10,10,10);

        Archer archer = new Archer(leatherBoots, leatherGloves, cooperPlatebody, cooperHelmet, cooperBow, 1.65);

        System.out.println( "archers performance:" + archer.getPerformance() );
    }

    public static void generateEquipment() throws IOException {
        Boots       = loadItems(0);
        Gloves      = loadItems(1);
        Platebodies = loadItems(2);
        Helmets     = loadItems(3);
        Weapons     = loadItems(4);
    }

    public static List<Warrior> generatePoblation(int poblationNumber, int warriorType) {
        List<Warrior> warriors = new ArrayList<>();
        for(int i = 0; i < poblationNumber; i++) {
            warriors.add(generateRandomWarrior(0));
        }
        return warriors;

    }

    private static Warrior generateRandomWarrior(int warriorType) {
          Boot warriorBoots = (Boot)Boots.get((int)(Math.floor(Math.random() * Boots.size())));
        Gloves warriorGloves = (ar.edu.itba.sia.Items.Gloves)Gloves.get((int)(Math.floor(Math.random() * Gloves.size())));
        Platebody warriorPlatebody = (Platebody) Platebodies.get((int)(Math.floor(Math.random() * Platebodies.size())));
        Helmet warriorHelmet = (Helmet) Helmets.get((int)(Math.floor(Math.random() * Helmets.size())));
        Weapon warriorWeapon = (Weapon) Weapons.get((int)(Math.floor(Math.random() * Weapons.size())));
        double warriorHeight = 1.65;//generate between max and min height TODO
        switch (warriorType) {
            case 0:
                return new Archer(warriorBoots, warriorGloves, warriorPlatebody, warriorHelmet, warriorWeapon, warriorHeight);
        }
        return null;
    }
}
