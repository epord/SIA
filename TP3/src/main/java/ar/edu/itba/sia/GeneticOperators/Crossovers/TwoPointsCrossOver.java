package ar.edu.itba.sia.GeneticOperators.Crossovers;

import ar.edu.itba.sia.GeneticOperators.Genes;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class TwoPointsCrossOver implements CrossOver {

    public List<Warrior> getCrossOver(Warrior w1, Warrior w2) {
        List<Warrior> newWarriors = new ArrayList<>();
        Warrior newWarrior1, newWarrior2;

        Genes[] genes = Genes.values();
        int locus1 = (int) (Math.random() * genes.length);
        int locus2 = (int) (Math.random() * genes.length);

        Boots boots1 = w1.getBoots();
        Boots boots2 = w2.getBoots();
        Gloves gloves1 = w1.getGloves();
        Gloves gloves2 = w2.getGloves();
        Platebody platebody1 = w1.getPlatebody();
        Platebody platebody2 = w2.getPlatebody();
        Helmet helmet1 = w1.getHelmet();
        Helmet helmet2 = w2.getHelmet();
        Weapon weapon1 = w1.getWeapon();
        Weapon weapon2 = w2.getWeapon();
        double height1 = w1.getHeight();
        double height2 = w2.getHeight();

        for (int i = Math.min(locus1, locus2); i <= Math.max(locus1, locus2); i++) {
            switch (genes[i]) {
                case BOOT:
                    boots1 = w2.getBoots();
                    boots2 = w1.getBoots();
                    break;
                case GLOVES:
                    gloves1 = w2.getGloves();
                    gloves2 = w1.getGloves();
                    break;
                case HEIGHT:
                    height1 = w2.getHeight();
                    height2 = w1.getHeight();
                    break;
                case HELMET:
                    helmet1 = w2.getHelmet();
                    helmet2 = w1.getHelmet();
                    break;
                case WEAPON:
                    weapon1 = w2.getWeapon();
                    weapon2 = w1.getWeapon();
                    break;
                case PLATEBODY:
                    platebody1 = w2.getPlatebody();
                    platebody2 = w1.getPlatebody();
                    break;
            }
        }
        newWarrior1 =  new Archer(boots1, gloves1, platebody1, helmet1, weapon1, height1);
        newWarrior2 =  new Archer(boots2, gloves2, platebody2, helmet2, weapon2, height2);

        newWarriors.add(newWarrior1);
        newWarriors.add(newWarrior2);
        return newWarriors;
    }
}