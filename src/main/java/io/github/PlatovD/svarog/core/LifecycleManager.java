package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.util.core.ReflectionUtils;

public final class LifecycleManager {

    public void afterCreate(Object instance, BeanDefinition definition) {
        if (definition.hasAfterCreate()) {
            ReflectionUtils.invokeMethod(instance, definition.getAfterCreate());
        }
    }

    public void beforeDestroy(Object instance, BeanDefinition definition) {
        if (definition.hasBeforeDestroy()) {
            ReflectionUtils.invokeMethod(instance, definition.getBeforeDestroy());
        }
    }
}