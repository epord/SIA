package ar.edu.itba.sia.Warriors;

import ar.edu.itba.sia.Items.*;

public class WarriorBuilder {
    public static Warrior buildWarrior(Boots boots, Gloves gloves, Platebody platebody, Helmet helmet,
                                       Weapon weapon, double height, WarriorType type) {
        Warrior newWarrior = null;
        switch(type) {
            case ASSASIN:
                break;
            case DEFENSOR:
                break;
            case SOLDIER:
                break;
            case ARCHER:
                newWarrior = new Archer(boots, gloves, platebody, helmet, weapon, height);
                break;
        }

        return newWarrior;
    }
}
