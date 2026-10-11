package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.util.core.ReflectionUtils;

public final class BeanFactory {

    public Object create(BeanDefinition definition) {
        return ReflectionUtils.newInstance(definition.getType());
    }
}
