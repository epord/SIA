package ar.edu.itba.sia.util;

import ar.edu.itba.sia.GeneticOperators.Interfaces.CustomizableSelection;
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
     * @return The configured selection method, with all its parameters as configured in the settings file.
     * @throws RuntimeException On configuration errors.
     */
    public static Selection getSelectionMethod(int selectionMethod) throws RuntimeException {
        return getSelectionMethodRecursive(getChosenSelectionMethods(selectionMethod));
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
     * @return The selection method(s).
     */
    private static List<SelectionMethod> getChosenSelectionMethods(int selectionMethod) {
        List<SelectionMethod> result = new ArrayList<>(2);
        if (selectionMethod == 0) {
            result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SELECTION_METHOD_1)));
            if (properties.containsKey(Constants.CROSSOVER_SECOND_SELECTION_METHOD_1)) {
                result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SECOND_SELECTION_METHOD_1)));
            }
        } else if (selectionMethod == 1) {
            result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SELECTION_METHOD_2)));
            if (properties.containsKey(Constants.CROSSOVER_SECOND_SELECTION_METHOD_2)) {
                result.add(SelectionMethod.fromSettings(getInt(Constants.CROSSOVER_SECOND_SELECTION_METHOD_2)));
            }
        }
        return result;
    }

    private static void validateSelectionParams() {
        int selectionMehtodsCount = 2;
        for (int i = 0; i < selectionMehtodsCount; i++) {
            List<SelectionMethod> selectionMethods = getChosenSelectionMethods(i);
            List<SelectionMethod> methodsThatNeedASecondMethod = new ArrayList<>();
            methodsThatNeedASecondMethod.add(SelectionMethod.RANKING);
            methodsThatNeedASecondMethod.add(SelectionMethod.BOLTZMANN);

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
}
