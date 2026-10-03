package io.github.PlatovD.svarog.resolver;

import io.github.PlatovD.svarog.definition.BeanDefinition;

import java.util.List;
import java.util.Map;

public final class ResolutionContext {

    private final List<BeanDefinition> input;

    private Map<Class<?>, List<BeanDefinition>> byType;
    private Map<String, BeanDefinition> byName;
    private Map<BeanDefinition, List<BeanDefinition>> resolvedDependencies;
    private List<BeanDefinition> creationOrder;

    public ResolutionContext(List<BeanDefinition> input) {
        this.input = input;
    }

    public List<BeanDefinition> getInput() {
        return input;
    }

    public Map<Class<?>, List<BeanDefinition>> getByType() {
        return byType;
    }

    public void setByType(Map<Class<?>, List<BeanDefinition>> byType) {
        this.byType = byType;
    }

    public Map<String, BeanDefinition> getByName() {
        return byName;
    }

    public void setByName(Map<String, BeanDefinition> byName) {
        this.byName = byName;
    }

    public Map<BeanDefinition, List<BeanDefinition>> getResolvedDependencies() {
        return resolvedDependencies;
    }

    public void setResolvedDependencies(Map<BeanDefinition, List<BeanDefinition>> resolvedDependencies) {
        this.resolvedDependencies = resolvedDependencies;
    }

    public List<BeanDefinition> getCreationOrder() {
        return creationOrder;
    }

    public void setCreationOrder(List<BeanDefinition> creationOrder) {
        this.creationOrder = creationOrder;
    }
}