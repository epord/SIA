package ar.edu.itba.sia.GeneticOperators.Mutations;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

/**
 * Multi-gene mutation.  Each gene may mutate with a particular probability independent of other genes.
 */
public class MultiGeneMutation implements Mutation {
    private static double p = 0.03; // TODO read this from file

    public Warrior mutate(Warrior warrior, List<Item> boots, List<Item> gloves, List<Item> platebodies,
                          List<Item> helmets, List<Item> weapons, double minHeight, double maxHeight) {

        double rand = Math.random();
        Boots newBoots = rand <= p ? (Boots) getRandomItem(boots) : warrior.getBoots();

        rand = Math.random();
        Gloves newGloves = rand <= p ? (Gloves) getRandomItem(gloves) : warrior.getGloves();

        rand = Math.random();
        Platebody newPlatebody = rand <= p ? (Platebody) getRandomItem(platebodies) : warrior.getPlatebody();

        rand = Math.random();
        Helmet newHelmet = rand <= p ? (Helmet) getRandomItem(helmets) : warrior.getHelmet();

        rand = Math.random();
        Weapon newWeapon = rand <= p ? (Weapon) getRandomItem(weapons) : warrior.getWeapon();

        rand = Math.random();
        double newHeight = rand <= p ? Math.random() * (maxHeight - minHeight) + minHeight : warrior.getHeight();

        return new Archer(newBoots, newGloves, newPlatebody, newHelmet, newWeapon, newHeight);
    }

    private static Item getRandomItem(List<Item> items) {
        return items.get((int) (Math.random() * items.size()));
    }
}
