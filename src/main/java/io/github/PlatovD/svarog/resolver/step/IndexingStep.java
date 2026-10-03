package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.exception.AmbiguousBeanException;
import io.github.PlatovD.svarog.resolver.ResolutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IndexingStep implements ResolutionStep {

    @Override
    public void apply(ResolutionContext context) {
        Map<Class<?>, List<BeanDefinition>> byType = new HashMap<>();
        Map<String, BeanDefinition> byName = new HashMap<>();

        for (BeanDefinition def : context.getInput()) {
            byType.computeIfAbsent(def.getType(), k -> new ArrayList<>()).add(def);

            String name = resolveName(def);
            BeanDefinition existing = byName.putIfAbsent(name, def);
            if (existing != null) {
                throw new AmbiguousBeanException(
                        "Duplicate bean name '" + name + "': " +
                                existing.getType().getName() + " and " +
                                def.getType().getName());
            }
        }

        context.setByType(byType);
        context.setByName(byName);
    }

    private String resolveName(BeanDefinition def) {
        if (def.hasName()) {
            return def.getName();
        }
        return def.getType().getName();
    }
}