package ar.edu.itba.sia.GeneticOperators.Selections;

/**
 * TODO rename {@link ar.edu.itba.sia.GeneticOperators.Interfaces.Selection} to SelectionMethod and rename this to something else
 */
public enum SelectionMethod {
    ELITE,
    ROULETTE,
    UNIVERSAL,
    TOURNAMENT_DETERMINISTIC,
    TOURNAMENT_PROBABILISTIC,
    RANKING,
    BOLTZMANN;

    /**
     * Gets a selection method from a number read from settings.
     *
     * @param methodNumber Method number, as read from settings file.
     * @return The corresponding selection method.
     * @throws ArrayIndexOutOfBoundsException If {@code methodNumber < 0 || methodNumber >= values().length}.
     */
    public static SelectionMethod fromSettings(int methodNumber) throws ArrayIndexOutOfBoundsException {
        return values()[methodNumber-1];
    }
}
