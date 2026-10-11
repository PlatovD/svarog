package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.util.core.ReflectionUtils;

public final class FieldInjector {

    public void inject(Object instance, BeanDefinition definition, BeanProvider provider) {
        for (Dependency dependency : definition.getDependencies()) {
            Object value = resolveDependency(dependency, provider);
            ReflectionUtils.setField(instance, dependency.getField(), value);
        }
    }

    private Object resolveDependency(Dependency dependency, BeanProvider provider) {
        if (dependency.hasQualifier()) {
            return provider.getBean(dependency.getQualifier());
        }
        return provider.getBean(dependency.getType());
    }
}