package ar.edu.itba.sia.GeneticOperators.Selections.Boltzmann;

import java.util.function.Function;

public enum TemperatureFunction {
    EXP {
        @Override
        public Function<Integer, Double> getTemperatureFunction() {
            return generation -> Math.exp(- (generation - 10000) / 1900.0);
        }
    },
    INVERSE {
        @Override
        public Function<Integer, Double> getTemperatureFunction() {
            return generation -> 1.0 / generation;
        }
    },
    LINEAL {
        @Override
        public Function<Integer, Double> getTemperatureFunction() {
            return generation -> - generation / 20.0 + 10000;
        }
    },
    LINEAL_SPLIT {
        @Override
        public Function<Integer, Double> getTemperatureFunction() {
            return generation -> Math.max(- generation * 3.0 + 1000, - generation / 12 + 240);
        }
    };

    public abstract Function<Integer, Double> getTemperatureFunction();


    /**
     * Gets a temperature function from a number read from settings.
     *
     * @param temperatureFunction Temperature function number, as read from settings file.
     * @return The corresponding temperature function.
     * @throws ArrayIndexOutOfBoundsException If {@code temperatureFunction < 0 || temperatureFunction >= values().length}.
     */
    public static TemperatureFunction fromSettings(int temperatureFunction) throws ArrayIndexOutOfBoundsException {
        return values()[temperatureFunction-1];
    }

}
