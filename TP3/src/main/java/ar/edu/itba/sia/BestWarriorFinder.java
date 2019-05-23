package ar.edu.itba.sia;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
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


    public static void main( String[] args ) throws IOException{

       // loadSettings("TP3/src/main/settings.properties");
        generateEquipment();

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
}
