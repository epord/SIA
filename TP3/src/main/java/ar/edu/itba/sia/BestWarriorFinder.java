package ar.edu.itba.sia;

import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;

import java.io.IOException;

import static ar.edu.itba.sia.util.Properties.loadSettings;

/**
 * Hello world!
 *
 */
public class BestWarriorFinder {
    public static void main( String[] args ) throws IOException{

       // loadSettings("TP3/src/main/settings.properties");
        Boot leatherBoots = new Boot(10,10,10,10,10);
        Gloves leatherGloves = new Gloves(10,10,10,10,10);
        Platebody cooperPlatebody = new Platebody(10,10,10,10,10);
        Helmet cooperHelmet = new Helmet(10,10,10,10,10);
        Weapon cooperBow = new Weapon(10,10,10,10,10);

        Archer archer = new Archer(leatherBoots, leatherGloves, cooperPlatebody, cooperHelmet, cooperBow, 1.65);

        System.out.println( "archers performance:" + archer.getPerformance() );
    }
}
