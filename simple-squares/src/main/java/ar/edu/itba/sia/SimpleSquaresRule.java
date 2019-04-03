package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.Optional;
import java.util.function.Function;

public class SimpleSquaresRule implements Rule{
    private String name;
    private Function<State, Optional<State>> function;

    public SimpleSquaresRule(String name, Function<State, Optional<State>> function) {
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
    public Optional<State> apply(State state) {
        return function.apply(state);
    }

    @Override
    public String toString() {
        return getName();
    }
}

