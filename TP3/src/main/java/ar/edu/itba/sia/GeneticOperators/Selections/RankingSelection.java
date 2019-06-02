package ar.edu.itba.sia.GeneticOperators.Selections;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CustomizableSelection;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

/**
 * Rank-based selection.
 *
 * @see <a href="https://stackoverflow.com/questions/20290831/how-to-perform-rank-based-selection-in-a-genetic-algorithm">StackOverflow related question</a>
 */
public class RankingSelection implements Selection {
    private final CustomizableSelection secondSelection;

    /**
     * @param secondSelection Customizable selection method to use with new fitnesses after ranking.
     */
    public RankingSelection(CustomizableSelection secondSelection) {
        this.secondSelection = secondSelection;
    }

    @Override
    public List<Warrior> select(List<Warrior> warriors, int quantity) {
        List<Warrior> sortedWarriors = new ArrayList<>(warriors);
        sortedWarriors.sort(Warrior.WORST_FITNESS_FIRST);
        double rankSum = (quantity+1) * quantity / 2.0; // 1 + 2 + ... + N = (N+1)* N / 2
        List<Double> customFitnesses = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            customFitnesses.add(i / rankSum);
        }

        return secondSelection.select(new ArrayList<>(sortedWarriors.subList(0, quantity)), quantity, customFitnesses);
    }
}
