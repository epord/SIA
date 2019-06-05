package ar.edu.itba.sia.GeneticOperators.Mutations;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;
import ar.edu.itba.sia.Warriors.WarriorType;
import ar.edu.itba.sia.util.Constants;
import ar.edu.itba.sia.util.Settings;

import static ar.edu.itba.sia.util.Constants.WARRIOR_TYPE;
import static ar.edu.itba.sia.util.Settings.getType;

public abstract class GeneralMutation implements Mutation {
    private static double probability = Settings.getDouble(Constants.MUTATION_PROBABILITY);
    private static WarriorType warriortype = getType(Settings.getInt(WARRIOR_TYPE));
    private static double minProbability = Settings.getDouble(Constants.MUTATION_MIN_PROBABILITY);
    private static boolean uniform = Settings.getBoolean(Constants.MUTATION_UNIFORMITY);
    private static int generations = Settings.getInt(Constants.MUTATION_UNIFORM_GENERATIONS); // Number of generations with constant probability
    private static int modifyingFunction = Settings.getInt(Constants.MUTATION_MODIFYING_FUNCTION);
    private static int generationCounter = 0;

    public void modifyProbability() {
        generationCounter ++;
        if(generationCounter == generations) {
            switch(modifyingFunction) {
                case 0:
                    decreasingProbabilityFunction();
                    break;
                case 1:
                    modifyingProbabilityFunction();
            }
            generationCounter = 0;
        }

    }

    private void decreasingProbabilityFunction() {
        probability = probability - (probability / 100);

        if(probability < minProbability) {
            probability = minProbability;
        }

    }

    private void modifyingProbabilityFunction() {
        double randomModifier = Math.random() / 100;
        probability += randomModifier;

        if(probability < minProbability) {
            probability = minProbability;
        }
    }

    public double getProbability() {
        return probability;
    }

    public boolean isUniform() {
        return uniform;
    }

    public static WarriorType getWarriortype() {
        return warriortype;
    }
}
