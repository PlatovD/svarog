package io.github.PlatovD.svarog.resolver;

import io.github.PlatovD.svarog.definition.BeanDefinition;

import java.util.List;
import java.util.Map;

public final class ResolutionResult {

    private final List<BeanDefinition> creationOrder;
    private final Map<BeanDefinition, List<BeanDefinition>> dependencies;
    private final Map<Class<?>, List<BeanDefinition>> byType;
    private final Map<String, BeanDefinition> byName;

    public ResolutionResult(
            List<BeanDefinition> creationOrder,
            Map<BeanDefinition, List<BeanDefinition>> dependencies,
            Map<Class<?>, List<BeanDefinition>> byType,
            Map<String, BeanDefinition> byName) {
        this.creationOrder = creationOrder;
        this.dependencies = dependencies;
        this.byType = byType;
        this.byName = byName;
    }

    public List<BeanDefinition> getCreationOrder() {
        return creationOrder;
    }

    public Map<BeanDefinition, List<BeanDefinition>> getDependencies() {
        return dependencies;
    }

    public Map<Class<?>, List<BeanDefinition>> getByType() {
        return byType;
    }

    public Map<String, BeanDefinition> getByName() {
        return byName;
    }
}