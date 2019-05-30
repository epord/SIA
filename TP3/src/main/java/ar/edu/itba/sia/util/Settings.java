package ar.edu.itba.sia.util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

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
        // TODO more
    }

    public static Properties get() {
        return properties;
    }

    public static int getInt(String property) {
        return Integer.parseInt(get().getProperty(property));
    }

    public static double getDouble(String property) {
        return Double.parseDouble(get().getProperty(property));
    }

    public static boolean getBoolean(String property) {
        return getInt(property) != 0;
    }
}
