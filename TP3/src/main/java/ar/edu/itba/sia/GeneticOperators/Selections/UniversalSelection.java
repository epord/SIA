package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CustomizableSelection;
import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.util.FitnessUtils;

import java.util.ArrayList;
import java.util.List;

public class UniversalSelection implements CustomizableSelection {

    /**
     * Select warriors as per universal selection, optionally allowing for custom fitnesses instead of {@link Warrior#getFitness()}
     * (eg. for Ranking or Boltzmann).
     *
     * @param warriors        Warriors to select from
     * @param quantity        Number of warriors to select
     * @param customFitnesses (Optional) Custom fitnesses to use. If null, will use warriors' fitness. If provided, should
     *                        correspond to each warrior's custom fitness by index.
     * @return The selected warriors.
     */
    public List<Warrior> select(List<Warrior> warriors, int quantity, List<Double> customFitnesses) {
        if (customFitnesses != null && customFitnesses.size() != warriors.size()) {
            throw new IllegalArgumentException(String.format("Warriors and custom fitnesses must match in length. %d != %d", warriors.size(), customFitnesses.size()));
        }

        double r = Math.random();
        double[] randoms = new double[quantity];
        initializeRandoms(randoms, quantity, r);
        List<Warrior> selectedWarriors = new ArrayList<>();
        int warriorIndex = 0;
        int randomIndex  = 0;
        double totalFitness = customFitnesses == null ? FitnessUtils.getTotalFitness(warriors) : FitnessUtils.getTotalCustomFitness(customFitnesses);
        //maybe shuffle warrior collection? TODO
        int selectedQuantity = 0;
        double accumulatedFitness = customFitnesses == null ? warriors.get(warriorIndex).getFitness() : customFitnesses.get(warriorIndex);

        while(selectedQuantity < quantity) {

            if(accumulatedFitness / totalFitness > randoms[randomIndex]) {
                selectedWarriors.add(warriors.get(warriorIndex));
                randomIndex++;
                selectedQuantity++;
            }
            else {
                warriorIndex++;
                accumulatedFitness += customFitnesses == null ? warriors.get(warriorIndex).getFitness() : customFitnesses.get(warriorIndex);
            }
        }

        return selectedWarriors;
    }

    protected void initializeRandoms(double[] randoms, int quantity, double r) {
        for(int i = 0; i < quantity; i++) {
            randoms[i] = (r + i) / quantity; // j = i + 1 -> j - 1 = i
        }
    }
}
