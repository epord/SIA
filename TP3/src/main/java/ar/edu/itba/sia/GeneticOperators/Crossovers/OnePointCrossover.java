package ar.edu.itba.sia.GeneticOperators.Crossovers;

import ar.edu.itba.sia.GeneticOperators.Genes;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CrossOver;
import ar.edu.itba.sia.Warriors.Archer;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class OnePointCrossover implements CrossOver {

    public List<Warrior> getCrossOver(Warrior w1, Warrior w2) {
        List<Warrior> newWarriors = new ArrayList<>();
        Genes genesValues[] = Genes.values();
        int locus = (int) (Math.random() * genesValues.length);
        Genes crossGene = genesValues[locus];
        Warrior newWarrior1 = null,
                newWarrior2 = null;
        switch(crossGene) {
            case BOOT:
               newWarrior1 =  new Archer(w2.getBoots(), w1.getGloves(), w1.getPlatebody(),
                                            w1.getHelmet(), w1.getWeapon(), w1.getHeight());

               newWarrior2 =  new Archer(w1.getBoots(), w2.getGloves(), w2.getPlatebody(),
                                            w2.getHelmet(),w2.getWeapon(), w2.getHeight());
                break;

            case GLOVES:
                newWarrior1 =  new Archer(w1.getBoots(), w2.getGloves(), w1.getPlatebody(),
                                            w1.getHelmet(), w1.getWeapon(), w1.getHeight());

                newWarrior2 =  new Archer(w2.getBoots(), w1.getGloves(), w2.getPlatebody(),
                                            w2.getHelmet(), w2.getWeapon(), w2.getHeight());
                break;

            case PLATEBODY:
                newWarrior1 =  new Archer(w1.getBoots(), w1.getGloves(), w2.getPlatebody(),
                                            w1.getHelmet(), w1.getWeapon(), w1.getHeight());

                newWarrior2 =  new Archer(w2.getBoots(), w2.getGloves(), w1.getPlatebody(),
                                             w2.getHelmet(), w2.getWeapon(), w2.getHeight());
                break;

            case HELMET:
                newWarrior1 =  new Archer(w1.getBoots(), w1.getGloves(), w1.getPlatebody(),
                                            w2.getHelmet(), w1.getWeapon(), w1.getHeight());

                newWarrior2 =  new Archer(w2.getBoots(), w2.getGloves(), w2.getPlatebody(),
                                            w1.getHelmet(), w2.getWeapon(), w2.getHeight());
                break;

            case WEAPON:
                newWarrior1 =  new Archer(w1.getBoots(), w1.getGloves(), w1.getPlatebody(),
                                             w1.getHelmet(), w2.getWeapon(), w1.getHeight());

                newWarrior2 =  new Archer(w2.getBoots(), w2.getGloves(), w2.getPlatebody(),
                                            w2.getHelmet(), w1.getWeapon(), w2.getHeight());
                break;

            case HEIGHT:
                newWarrior1 =  new Archer(w1.getBoots(), w1.getGloves(), w1.getPlatebody(),
                                                w1.getHelmet(), w1.getWeapon(), w2.getHeight());

                newWarrior2 =  new Archer(w2.getBoots(), w2.getGloves(), w2.getPlatebody(),
                                             w2.getHelmet(), w2.getWeapon(), w1.getHeight());
                break;


        }
        newWarriors.add(newWarrior1);
        newWarriors.add(newWarrior2);
        return newWarriors;
    }
}