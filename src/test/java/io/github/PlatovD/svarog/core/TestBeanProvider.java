package io.github.PlatovD.svarog.core;

import java.util.HashMap;
import java.util.Map;

class TestBeanProvider implements BeanProvider {

    private final Map<Class<?>, Object> byType = new HashMap<>();
    private final Map<String, Object> byName = new HashMap<>();

    TestBeanProvider put(Class<?> type, Object bean) {
        byType.put(type, bean);
        return this;
    }

    TestBeanProvider put(String name, Object bean) {
        byName.put(name, bean);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        return (T) byType.get(type);
    }

    @Override
    public Object getBean(String name) {
        return byName.get(name);
    }
}