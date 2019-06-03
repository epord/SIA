package ar.edu.itba.sia.util;

import ar.edu.itba.sia.GeneticOperators.EndConditions.*;
import ar.edu.itba.sia.GeneticOperators.Interfaces.CustomizableSelection;
import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.GeneticOperators.Interfaces.Selection;
import ar.edu.itba.sia.GeneticOperators.Selections.*;
import ar.edu.itba.sia.GeneticOperators.Selections.Boltzmann.BoltzmannSelection;
import ar.edu.itba.sia.GeneticOperators.Selections.Boltzmann.TemperatureFunction;
import ar.edu.itba.sia.Warriors.WarriorType;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import static ar.edu.itba.sia.util.Constants.*;

public class Settings {
    private static final Properties properties = new Properties();

    public static void loadSettings(String propertiesPath) throws IOException {
        properties.load(new FileReader(Paths.get(propertiesPath).toFile()));
        validate();
    }

    /**
     * Perform some basic parameter validation.
     *
     * @throws IllegalArgumentException If validation fails.
     */
    private static void validate() throws IllegalArgumentException {
        validateRequiredParams();

        double attackMult = getDouble(Constants.ATTACK_MULTIPLIER),
            defenseMult = getDouble(Constants.DEFENSE_MULTIPLIER);
        if (attackMult + defenseMult != 1) {
            throw new IllegalArgumentException(String.format("Attack and defense multipliers must add up to 1; %g + %g != 1", attackMult, defenseMult));
        }

        if (!getBoolean(Constants.MUTATION_UNIFORMITY)) {
            if (!properties.contains(Constants.MUTATION_UNIFORM_GENERATIONS)) {
                throw new IllegalArgumentException(String.format("%s=0, but %s is not set", Constants.MUTATION_UNIFORMITY, Constants.MUTATION_UNIFORM_GENERATIONS));
            } else if (getInt(Constants.MUTATION_UNIFORM_GENERATIONS) <= 0) {
                throw new IllegalArgumentException(Constants.MUTATION_UNIFORM_GENERATIONS + " must be > 0");
            }
        }

        validateWithinRange(Constants.CROSSOVER_A, 0, 1);
        validateWithinRange(Constants.REPLACEMENT_B, 0, 1);

        validateSelectionParams();
        // TODO more
    }

    public static int getInt(String property) {
        return Integer.parseInt(properties.getProperty(property));
    }

    public static double getDouble(String property) {
        return Double.parseDouble(properties.getProperty(property));
    }

    public static boolean getBoolean(String property) {
        return getInt(property) != 0;
    }

    /**
     * Get the configured selection method, with its configured parameters.
     *
     * @param mode <ul>
     *             <li>1 for selection method(s) 1</li>
     *             <li>2 for selection method(s) 2</li>
     *             <li>3 for replacement method(s) 1</li>
     *             <li>4 for replacement method(s) 2</li>
     * </ol>
     * @return The configured selection method, with all its parameters as configured in the settings file.
     * @throws RuntimeException On configuration errors.
     */
    public static Selection getSelectionMethod(int mode) throws RuntimeException {
        return getSelectionMethodRecursive(getChosenSelectionMethods(mode));
    }

    private static Function<Integer, Double> getBoltzmannTemperatureFunction() {
        return TemperatureFunction.fromSettings(getInt(Constants.BOLTZMANN_TEMPERATURE_FUNCTION)).getTemperatureFunction();
    }

    private static Selection getSelectionMethodRecursive(List<SelectionMethod> selectionMethods) {
        switch (selectionMethods.get(0)) {
            case ELITE:
                return new EliteSelection();
            case ROULETTE:
                return new RouletteSelection();
            case UNIVERSAL:
                return new UniversalSelection();
            case TOURNAMENT_DETERMINISTIC:
                return new DeterministicTournamentSelection(getInt(Constants.TOURNAMENT_DETERMINISTIC_M));
            case TOURNAMENT_PROBABILISTIC:
                return new ProbabilisticTournamentSelection(getDouble(Constants.TOURNAMENT_RANDOM_PROBABILITY));
            case RANKING:
                return new RankingSelection((CustomizableSelection) (getSelectionMethodRecursive(selectionMethods.subList(1, 2))));
            case BOLTZMANN:
                return new BoltzmannSelection((CustomizableSelection) (getSelectionMethodRecursive(selectionMethods.subList(1, 2))), getBoltzmannTemperatureFunction());
            default:
                throw new UnsupportedOperationException("Not implemented");
        }
    }

    /**
     * Get the 1 or 2 selection methods chosen in the settings.
     *
     * @param mode See {@link #getSelectionMethod(int)}.
     * @return The selection method(s).
     */
    private static List<SelectionMethod> getChosenSelectionMethods(int mode) {
        List<SelectionMethod> result = new ArrayList<>(2);
        switch(mode) {
            case 1:
                result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SELECTION_METHOD_1)));
                if (properties.containsKey(Constants.CROSSOVER_SECOND_SELECTION_METHOD_1)) {
                    result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SECOND_SELECTION_METHOD_1)));
                }
                break;
            case 2:
                result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SELECTION_METHOD_2)));
                if (properties.containsKey(Constants.CROSSOVER_SECOND_SELECTION_METHOD_2)) {
                    result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SECOND_SELECTION_METHOD_2)));
                }
            case 3:
                result.add(SelectionMethod.fromSettings(getInt(Constants.REPLACEMENT_SELECTION_METHOD_1)));
                if (properties.containsKey(Constants.REPLACEMENT_SECOND_SELECTION_METHOD_1)) {
                    result.add(SelectionMethod.fromSettings(getInt(Constants.REPLACEMENT_SECOND_SELECTION_METHOD_1)));
                }
                break;
            case 4:
                result.add(SelectionMethod.fromSettings(getInt(Constants.REPLACEMENT_SELECTION_METHOD_2)));
                if (properties.containsKey(Constants.REPLACEMENT_SECOND_SELECTION_METHOD_2)) {
                    result.add(SelectionMethod.fromSettings(getInt(Constants.REPLACEMENT_SECOND_SELECTION_METHOD_2)));
                }
                break;
            default:
                throw new IllegalArgumentException("Valid values are 1 through 4");
        }
        return result;
    }

    public static EndCondition getEndCondition(int type) {

        EndCondition endCondition = null;
        switch(type) {
            case 0:
                int maxGenerations = getInt(MAX_GENERATIONS);
                endCondition = new MaxGenerationsEndCondition(maxGenerations);
                break;
            case 1:
                double percentage = getDouble(STRUCTURAL_PERCENTAGE);
                endCondition = new StructuralEndCondition(percentage);
                break;

            case 2:
                int consecutiveGenerations = getInt(CONTENT_CONSECUTIVE_GENERATIONS);
                endCondition = new ContentEndCondition(consecutiveGenerations);
                break;

            case 3:
                double bestFitness = getDouble(NEAR_OPTIMAL_FITNESS);
                double delta = getDouble(NEAR_OPTIMAL_DELTA);
                endCondition = new NearOptimalEndCondition(bestFitness, delta);
                break;
            case 4:
                maxGenerations = getInt(MAX_GENERATIONS);
                EndCondition maxGenerationEndCondition = new MaxGenerationsEndCondition(maxGenerations);
                percentage = getDouble(STRUCTURAL_PERCENTAGE);
                EndCondition structuralEndCondition = new StructuralEndCondition(percentage);
                consecutiveGenerations = getInt(CONTENT_CONSECUTIVE_GENERATIONS);
                EndCondition contentEndCondition = new ContentEndCondition(consecutiveGenerations);
                bestFitness = getDouble(NEAR_OPTIMAL_FITNESS);
                delta = getDouble(NEAR_OPTIMAL_DELTA);
                EndCondition nearOptimalEndCondition = new NearOptimalEndCondition(bestFitness, delta);
                endCondition = new EndConditionsCombiner(maxGenerationEndCondition, structuralEndCondition,
                                                        contentEndCondition, nearOptimalEndCondition);
                break;

        }

        return endCondition;
    }
    private static void validateSelectionParams() {
        List<SelectionMethod> methodsThatNeedASecondMethod = new ArrayList<>();
        methodsThatNeedASecondMethod.add(SelectionMethod.RANKING);
        methodsThatNeedASecondMethod.add(SelectionMethod.BOLTZMANN);
        int selectionMehtodsCount = 4;
        for (int i = 1; i <= selectionMehtodsCount; i++) {
            List<SelectionMethod> selectionMethods = getChosenSelectionMethods(i);

            if (selectionMethods.size() == 1 && methodsThatNeedASecondMethod.contains(selectionMethods.get(0))) {
                throw new IllegalArgumentException(selectionMethods.get(0).name() + " requires a secondary selection method but none was chosen");
            }
        }
    }

    /**
     * Validate that all required parameters are present.
     */
    private static void validateRequiredParams() {
        String[] requiredParams = new String[] {
                Constants.ATTACK_MULTIPLIER, Constants.DEFENSE_MULTIPLIER, Constants.STRENGTH_MULTIPLIER, Constants.AGILITY_MULTIPLIER,
                Constants.EXPERTISE_MULTIPLIER, Constants.RESISTANCE_MULTIPLIER, Constants.HEALTH_MULTIPLIER, Constants.MIN_HEIGHT,
                Constants.MAX_HEIGHT, Constants.CROSSOVER_PROBABILITY, Constants.CROSSOVER_A, Constants.CROSSOVER_SELECTION_METHOD_1,
                Constants.MUTATION_PROBABILITY, Constants.MUTATION_UNIFORMITY, Constants.REPLACEMENT_GENERATION_GAP, Constants.REPLACEMENT_B,
                Constants.REPLACEMENT_SELECTION_METHOD_1
        };
        for (String requiredParam : requiredParams) {
            if (!properties.containsKey(requiredParam)) {
                throw new IllegalArgumentException(requiredParam + " is required");
            }
        }
    }

    public static WarriorType getType(int type) {
        WarriorType currType = null;
        switch(type) {
            case 0:
                currType = WarriorType.SOLDIER;
                break;

            case 1:
                currType = WarriorType.ARCHER;
                break;

            case 2:
                currType = WarriorType.DEFENSOR;
                break;

            case 3:
                currType = WarriorType.ASSASIN;
                break;
        }
        return currType;
    }

    private static void validateWithinRange(String propertyName, double min, double max) {
        double num = getDouble(propertyName);
        if (num < min || num > max) {
            throw new IllegalArgumentException(String.format("%s must be within %g and %g but is %g", propertyName, min, max, num));
        }
    }
}
