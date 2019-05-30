package ar.edu.itba.sia.GeneticOperators.Mutations;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

/**
 * Multi-gene mutation.  Each gene may mutate with a particular probability independent of other genes.
 */
public class MultiGeneMutation extends GeneralMutation implements Mutation {

    public Warrior mutate(Warrior warrior, List<Item> boots, List<Item> gloves, List<Item> platebodies,
                          List<Item> helmets, List<Item> weapons, double minHeight, double maxHeight) {

        double rand = Math.random();
        double currentProbability = getProbability();
        Boots newBoots = rand <= currentProbability ? (Boots) getRandomItem(boots) : warrior.getBoots();

        rand = Math.random();
        Gloves newGloves = rand <= currentProbability ? (Gloves) getRandomItem(gloves) : warrior.getGloves();

        rand = Math.random();
        Platebody newPlatebody = rand <= currentProbability ? (Platebody) getRandomItem(platebodies) : warrior.getPlatebody();

        rand = Math.random();
        Helmet newHelmet = rand <= currentProbability ? (Helmet) getRandomItem(helmets) : warrior.getHelmet();

        rand = Math.random();
        Weapon newWeapon = rand <= currentProbability ? (Weapon) getRandomItem(weapons) : warrior.getWeapon();

        rand = Math.random();
        double newHeight = rand <= currentProbability ? Math.random() * (maxHeight - minHeight) + minHeight : warrior.getHeight();

        if(!isUniform()) {
            modifyProbability();
        }

        return new Archer(newBoots, newGloves, newPlatebody, newHelmet, newWeapon, newHeight);
    }

    private static Item getRandomItem(List<Item> items) {
        return items.get((int) (Math.random() * items.size()));
    }
}
