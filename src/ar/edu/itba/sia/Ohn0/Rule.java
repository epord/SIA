package ar.edu.itba.sia.Ohn0;

import java.util.Optional;
import java.util.function.Function;

public class Rule implements ar.edu.itba.sia.gps.api.Rule {
    private String name;
    private Function<ar.edu.itba.sia.gps.api.State, Optional<ar.edu.itba.sia.gps.api.State>> function;

    public Rule(String name, Function<ar.edu.itba.sia.gps.api.State, Optional<ar.edu.itba.sia.gps.api.State>> function) {
        this.name = name;
        this.function = function;
    }

    @Override
    public Integer getCost() {
        return 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<ar.edu.itba.sia.gps.api.State> apply(ar.edu.itba.sia.gps.api.State state) {
        return function.apply(state);
    }
}
