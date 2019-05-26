package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversalSelection implements Selection {
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        double r = Math.random();
        double randoms[] = new double[quantity];
        initializeRandoms(randoms, quantity, r);
        List<Warrior> selectedWarriors = new ArrayList<>();
        int warriorIndex = 0;
        int randomIndex  = 0;
        double cumulatedPerformance = 0.0;
        double totalPerformance = getTotalPerformance(warriors);
        //maybe shuffle warrior collection? TODO
        int selectedQuantity = 0;
        cumulatedPerformance = warriors.get(warriorIndex).getPerformance();

        while(selectedQuantity < quantity) {

            if(cumulatedPerformance / totalPerformance > randoms[randomIndex]) {
                selectedWarriors.add(warriors.get(warriorIndex));
                randomIndex++;
                selectedQuantity++;
            }
            else {
                warriorIndex++;
                cumulatedPerformance += warriors.get(warriorIndex).getPerformance();

            }
        }

        return selectedWarriors;
    }

    private static void initializeRandoms(double randoms[], int quantity, double r) {
        for(int i = 0; i < quantity; i++) {
            randoms[i] = (r + i) / quantity; // j = i + 1 -> j - 1 = i
        }
    }

    private static double getTotalPerformance(List<Warrior> warriors) {
        double totalPerformance = 0.0;
        for(Warrior w : warriors) {
            totalPerformance += w.getPerformance();
        }
        return totalPerformance;
    }

}
