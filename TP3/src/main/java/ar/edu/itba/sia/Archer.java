package ar.edu.itba.sia;

import ar.edu.itba.sia.Items.*;

public class Archer extends Warrior {
    private static final double ATTACK_MULTIPLIER = 0.9;//should read from properties? TODO
    private static final double DEFENSE_MULTIPLIER = 0.1;//should read from properties? TODO

    public Archer(Boot boots, Gloves gloves, Platebody platebody, Helmet helmet, Weapon weapon, double height) {
        super(boots, gloves, platebody, helmet, weapon, height);
    }

    public double getPerformance() {
        return ATTACK_MULTIPLIER * getAttack() + DEFENSE_MULTIPLIER * getDefense();
    }
}
