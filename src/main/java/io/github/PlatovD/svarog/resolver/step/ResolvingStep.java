package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.util.resolver.DependencyResolver;
import io.github.PlatovD.svarog.resolver.ResolutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResolvingStep implements ResolutionStep {

    @Override
    public void apply(ResolutionContext context) {
        Map<Class<?>, List<BeanDefinition>> byType = context.getByType();
        Map<String, BeanDefinition> byName = context.getByName();
        Map<BeanDefinition, List<BeanDefinition>> resolved = new HashMap<>();

        for (BeanDefinition def : context.getInput()) {
            List<BeanDefinition> deps = new ArrayList<>();
            for (Dependency dependency : def.getDependencies()) {
                BeanDefinition dep = DependencyResolver.resolve(dependency, byType, byName);
                deps.add(dep);
            }
            resolved.put(def, deps);
        }

        context.setResolvedDependencies(resolved);
    }
}