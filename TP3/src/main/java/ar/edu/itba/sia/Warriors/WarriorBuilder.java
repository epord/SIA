package ar.edu.itba.sia.Warriors;

import ar.edu.itba.sia.Items.*;

public class WarriorBuilder {
    public static Warrior buildWarrior(Boots boots, Gloves gloves, Platebody platebody, Helmet helmet,
                                       Weapon weapon, double height, WarriorType type) {
        Warrior newWarrior = null;
        switch(type) {
            case SOLDIER:
                break;
            case ARCHER:
                newWarrior = new Archer(boots, gloves, platebody, helmet, weapon, height);
                break;
            case DEFENSOR:
                break;
            case ASSASIN:
                break;

        }

        return newWarrior;
    }
}
