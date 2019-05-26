package ar.edu.itba.sia.Warriors;

import ar.edu.itba.sia.Items.*;

public class Archer extends Warrior {
    private static final double ATTACK_MULTIPLIER = 0.9; //getDouble(Constants.ATTACK_MULTIPLIER);
    private static final double DEFENSE_MULTIPLIER = 0.1; //getDouble(Constants.DEFENSE_MULTIPLIER);

    public Archer(Boots boots, Gloves gloves, Platebody platebody, Helmet helmet, Weapon weapon, double height) {
        super(boots, gloves, platebody, helmet, weapon, height);
    }

    public double getPerformance() {
        return ATTACK_MULTIPLIER * getAttack() + DEFENSE_MULTIPLIER * getDefense();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || !o.getClass().equals(getClass())) {
            return false;
        }

        Archer archer = (Archer)o;
        return archer.getPerformance() == getPerformance();
    }
}
