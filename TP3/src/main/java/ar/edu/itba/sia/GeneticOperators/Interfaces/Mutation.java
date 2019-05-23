package ar.edu.itba.sia.GeneticOperators.Interfaces;

import ar.edu.itba.sia.Items.Item;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.List;

public interface Mutation {
    public Warrior mutate(Warrior warrior, List<Item> Boot, List<Item> Gloves, List<Item> Platebodies,
                          List<Item> Helmets, List<Item> Weapon, double minHeight, double maxHeight);
}
