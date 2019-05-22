package ar.edu.itba.sia.util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Properties {
    private static final java.util.Properties properties = new java.util.Properties();

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
        // TODO more
    }

    public static java.util.Properties get() {
        return properties;
    }

    public static int getInt(String property) {
        return Integer.parseInt(get().getProperty(property));
    }

    public static double getDouble(String property) {
        return Double.parseDouble(get().getProperty(property));
    }
}
