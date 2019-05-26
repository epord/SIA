package ar.edu.itba.sia.GeneticOperators.Crossovers;

import ar.edu.itba.sia.GeneticOperators.Genes;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class UniformCrossover implements CrossOver {

    public static final double p = 0.5;

    public List<Warrior> getCrossOver(Warrior w1, Warrior w2) {
        List<Warrior> newWarriors = new ArrayList<>();
        Warrior newWarrior1, newWarrior2;

        double rand = Math.random();
        Boots boots1 =  rand > p ? w1.getBoots() : w2.getBoots();
        Boots boots2 =  rand <= p ? w2.getBoots() : w1.getBoots();

        rand = Math.random();
        Gloves gloves1 =  rand > p ? w1.getGloves() : w2.getGloves();
        Gloves gloves2 =  rand <= p ? w2.getGloves() : w1.getGloves();

        rand = Math.random();
        Platebody platebody1 =  rand > p ? w1.getPlatebody() : w2.getPlatebody();
        Platebody platebody2 =  rand <= p ? w2.getPlatebody() : w1.getPlatebody();

        rand = Math.random();
        Helmet helmet1 =  rand > p ? w1.getHelmet() : w2.getHelmet();
        Helmet helmet2 =  rand <= p ? w2.getHelmet() : w1.getHelmet();

        rand = Math.random();
        Weapon weapon1 =  rand > p ? w1.getWeapon() : w2.getWeapon();
        Weapon weapon2 =  rand <= p ? w2.getWeapon() : w1.getWeapon();

        rand = Math.random();
        double height1 =  rand > p ? w1.getHeight() : w2.getHeight();
        double height2 =  rand <= p ? w2.getHeight() : w1.getHeight();

        newWarrior1 =  new Archer(boots1, gloves1, platebody1, helmet1, weapon1, height1);
        newWarrior2 =  new Archer(boots2, gloves2, platebody2, helmet2, weapon2, height2);

        newWarriors.add(newWarrior1);
        newWarriors.add(newWarrior2);
        return newWarriors;
    }

}