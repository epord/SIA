package ar.edu.itba.sia.GeneticOperators;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class GenMutation implements Mutation {
    private static final int genes = 5;

    public Warrior mutate(Warrior warrior, List<Item> Boots, List<Item> Glove, List<Item> Platebodies,
                          List<Item> Helmets, List<Item> Weapons, double minHeight, double maxHeight) {
        List<Warrior> newWarriors = new ArrayList<>();
        Genes genesValues[] = Genes.values();
        int locus = (int) Math.random() * genes;
        Genes crossGene = genesValues[locus];
        Warrior newWarrior= null;
        switch(crossGene) {
            case BOOT:
                Boot newBoot = (Boot)getRandomItem(Boots);
                newWarrior =  new Archer(newBoot, warrior.getGloves(), warrior.getPlatebody(),
                                        warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight());
                break;

            case GLOVES:
                Gloves newGlove = (Gloves) getRandomItem(Glove);
                newWarrior =  new Archer(warrior.getBoots(), newGlove, warrior.getPlatebody(),
                        warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight());
                break;

            case PLATEBODY:
                Platebody newPlatebody = (Platebody) getRandomItem(Platebodies);
                newWarrior =  new Archer(warrior.getBoots(), warrior.getGloves(), newPlatebody,
                        warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight());
                break;

            case HELMET:
                Helmet newHelmet = (Helmet) getRandomItem(Helmets);
                newWarrior =  new Archer(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                                            newHelmet, warrior.getWeapon(), warrior.getHeight());
                break;

            case WEAPON:
                Weapon newWeapon = (Weapon) getRandomItem(Weapons);
                newWarrior =  new Archer(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                        warrior.getHelmet(), newWeapon, warrior.getHeight());
                break;

            case HEIGHT:
                double newHeight = Math.random() * (maxHeight - minHeight) + minHeight;
                newWarrior =  new Archer(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                                warrior.getHelmet(), warrior.getWeapon(), newHeight);

                break;


        }

        return newWarrior;
    }

    private static Item getRandomItem(List<Item> items) {

        return items.get((int)Math.random() * items.size());
    }
}
