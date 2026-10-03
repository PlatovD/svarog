package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.resolver.ResolutionContext;

@FunctionalInterface
public interface ResolutionStep {

    void apply(ResolutionContext context);

    default String name() {
        return getClass().getSimpleName();
    }
}