package ar.edu.itba.sia.Warriors;

import ar.edu.itba.sia.Items.*;

public abstract class Warrior {
    //Items
    private Boots boots;
    private Gloves gloves;
    private Platebody platebody;
    private Weapon weapon;
    private Helmet helmet;

    private double height;

    //Stats
    private double strength;
    private double agility;
    private double expertise;
    private double resistance;
    private double hitpoints;


    protected Warrior(Boots boots, Gloves gloves, Platebody platebody, Helmet helmet, Weapon weapon, double height) {
        this.boots     = boots;
        this.gloves    = gloves;
        this.platebody = platebody;
        this.helmet    = helmet;
        this.weapon    = weapon;
        this.height    = height;

        setStats();
    }

    public Boots getBoots() {
        return boots;
    }

    public Gloves getGloves() {
        return gloves;
    }

    public Platebody getPlatebody() {
        return platebody;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public double getHeight() {
        return height;
    }

    public double getATM() {
        double result = 3 * height - 5;
        return 0.5 - Math.pow(result, 4) + Math.pow(result, 2) + height/2;
    }

    public double getDEM() {
        double result = 3 * height - 5;
        return 2 + Math.pow(result, 4) - Math.pow(result, 2) - height/2;
    }

    public double getStrength() {
        return strength;
    }


    public double getAgility() {
        return agility;
    }

    public double getExpertise() {
        return expertise;
    }


    public double getResistance() {
        return resistance;
    }


    public double getHitPoints() {
        return hitpoints;
    }

    private void setStrength() {
        double totalStrength = 0;
        totalStrength += boots.getStrength() + gloves.getStrength() + platebody.getStrength() +
                helmet.getStrength() + weapon.getStrength();
        this.strength = totalStrength;
    }
    private void setAgility() {
        double totalAgility = 0;
        totalAgility += boots.getAgility() + gloves.getAgility() + platebody.getAgility() +
                helmet.getAgility() + weapon.getAgility();
        this.agility = totalAgility;
    }

    private void setExpertise() {
        double totalExpertise = 0;
        totalExpertise += boots.getExpertise() + gloves.getExpertise() + platebody.getExpertise() +
                helmet.getExpertise() + weapon.getExpertise();
        this.expertise = totalExpertise;
    }

    private void setResistance() {
        double totalResistance = 0;
        totalResistance += boots.getResistance() + gloves.getResistance() + platebody.getResistance() +
                helmet.getResistance() + weapon.getResistance();
        this.resistance = totalResistance;
    }
    private void setHitpoints() {
        double totalHitpoints = 0;
        totalHitpoints += boots.getHitPoints() + gloves.getHitPoints() + platebody.getHitPoints() +
                helmet.getHitPoints() + weapon.getHitPoints();
        this.hitpoints = totalHitpoints;
    }

    public double getAttack() {
        return (getAgility() + getExpertise()) * getStrength() * getATM();

    }

    public double getDefense() {
        return (getResistance() + getExpertise()) * getHitPoints() * getDEM();
    }

    private void setStats() {
        setStrength();
        setAgility();
        setExpertise();
        setResistance();
        setHitpoints();
    }
    public abstract double getPerformance();

    @Override
    public boolean equals(Object o) {
        if( this == o) {
            return true;
        }
        if(o == null || !(o instanceof Warrior)) {
            return false;
        }
        Warrior warrior = (Warrior) o;
        return warrior.equals(this);
    }

    @Override
    public String toString() {
        return "Warrior{" + ",\n" +
                "   boots = " + boots + ",\n" +
                "   gloves = " + gloves + ",\n" +
                "   platebody = " + platebody + ",\n" +
                "   weapon = " + weapon + ",\n" +
                "   helmet = " + helmet + ",\n" +
                "   height = " + height + ",\n" +
                "   strength = " + strength + ",\n" +
                "   agility = " + agility + ",\n" +
                "   expertise = " + expertise + ",\n" +
                "   resistance = " + resistance + ",\n" +
                "   hitpoints = " + hitpoints + ",\n" +
                "   performance = " + getPerformance() + "\n" +
                '}';
    }
}
