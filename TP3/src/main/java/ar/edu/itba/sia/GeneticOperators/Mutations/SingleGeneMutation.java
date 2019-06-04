package ar.edu.itba.sia.GeneticOperators.Mutations;

import ar.edu.itba.sia.GeneticOperators.Genes;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

import static ar.edu.itba.sia.Warriors.WarriorBuilder.buildWarrior;

/**
 * Single-gene mutation.  Chooses a single random locus and mutates it.
 */
public class SingleGeneMutation extends GeneralMutation implements Mutation {

    public Warrior mutate(Warrior warrior, List<Item> boots, List<Item> gloves, List<Item> platebodies,
                          List<Item> helmets, List<Item> weapons, double minHeight, double maxHeight) {
        Genes[] genes = Genes.values();
        int locus = (int) (Math.random() * genes.length);
        Genes crossGene = genes[locus];
        Warrior newWarrior= buildWarrior(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight(), getWarriortype());
        double probability = getProbability();
        double rand = Math.random();
        if(rand <= probability) {
            switch (crossGene) {
                case BOOT:
                    Boots newBoots = (Boots) getRandomItem(boots);
                    newWarrior = buildWarrior(newBoots, warrior.getGloves(), warrior.getPlatebody(),
                            warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight(), getWarriortype());
                    break;

                case GLOVES:
                    Gloves newGlove = (Gloves) getRandomItem(gloves);
                    newWarrior = buildWarrior(warrior.getBoots(), newGlove, warrior.getPlatebody(),
                            warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight(), getWarriortype());
                    break;

                case PLATEBODY:
                    Platebody newPlatebody = (Platebody) getRandomItem(platebodies);
                    newWarrior = buildWarrior(warrior.getBoots(), warrior.getGloves(), newPlatebody,
                            warrior.getHelmet(), warrior.getWeapon(), warrior.getHeight(), getWarriortype());
                    break;

                case HELMET:
                    Helmet newHelmet = (Helmet) getRandomItem(helmets);
                    newWarrior = buildWarrior(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                            newHelmet, warrior.getWeapon(), warrior.getHeight(), getWarriortype());
                    break;

                case WEAPON:
                    Weapon newWeapon = (Weapon) getRandomItem(weapons);
                    newWarrior = buildWarrior(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                            warrior.getHelmet(), newWeapon, warrior.getHeight(), getWarriortype());
                    break;

                case HEIGHT:
                    double newHeight = Math.random() * (maxHeight - minHeight) + minHeight;
                    newWarrior = buildWarrior(warrior.getBoots(), warrior.getGloves(), warrior.getPlatebody(),
                            warrior.getHelmet(), warrior.getWeapon(), newHeight, getWarriortype());

                    break;
            }
        }

        if(!isUniform()) {
            modifyProbability();
        }

        return newWarrior;
    }

    private static Item getRandomItem(List<Item> items) {
        return items.get((int) (Math.random() * items.size()));
    }
}
