package ar.edu.itba.sia.GeneticOperators.Mutations;

import ar.edu.itba.sia.GeneticOperators.Interfaces.Mutation;

public abstract class GeneralMutation implements Mutation {
    private static double probability = 0.03; // TODO read this from file
    private static double minProbability = 0.04; //TODO read this from file
    private static boolean uniform = true; //TODO read this ftom file
    private static int generations = 1; //number of generations with constant probability TODO read from file
    private static int modifyingFunction = 0; //TODO read from file can be an enum
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
        probability = probability - (probability / 10);

        if(probability < minProbability) {
            probability = minProbability;
        }

    }



    private void modifyingProbabilityFunction() {
        double randomModifier = Math.random() / 10;
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

}
