package ar.edu.itba.sia.Items;


public abstract class Item {
    private double strength;
    private double agility;
    private double expertise;
    private double resistance;
    private double hitpoints;

    protected Item(double strength, double agility, double expertise, double resistance, double hitpoints) {
        this.strength   = strength;
        this.agility    = agility;
        this.expertise  = expertise;
        this.resistance = resistance;
        this.hitpoints  = hitpoints;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getAgility() {
        return agility;
    }

    public void setAgility(double agility) {
        this.agility = agility;
    }

    public double getExpertise() {
        return expertise;
    }

    public void setExpertise(double expertise) {
        this.expertise = expertise;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public double getHitPoints() {
        return hitpoints;
    }

    public void setHitpoints(double hitpoints) {
        this.hitpoints = hitpoints;
    }

    @Override
    public String toString() {
        return "Item{" + ",\n" +
                "      strength = " + strength + ",\n" +
                "      agility = " + agility + ",\n" +
                "      expertise = " + expertise + ",\n" +
                "      resistance = " + resistance + ",\n" +
                "      hitpoints = " + hitpoints + "\n" +
                "   }";
    }
}


