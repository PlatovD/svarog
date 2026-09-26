package io.github.PlatovD.svarog.definition;

import java.lang.reflect.Method;
import java.util.List;

public final class BeanDefinition {

    private final Class<?> type;
    private final String name;
    private final Scope scope;
    private final boolean lazy;
    private final boolean primary;
    private final List<Dependency> dependencies;
    private final Method afterCreate;
    private final Method beforeDestroy;

    BeanDefinition(
            Class<?> type,
            String name,
            Scope scope,
            boolean lazy,
            boolean primary,
            List<Dependency> dependencies,
            Method afterCreate,
            Method beforeDestroy) {
        this.type = type;
        this.name = name;
        this.scope = scope;
        this.lazy = lazy;
        this.primary = primary;
        this.dependencies = dependencies;
        this.afterCreate = afterCreate;
        this.beforeDestroy = beforeDestroy;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Scope getScope() {
        return scope;
    }

    public boolean isLazy() {
        return lazy;
    }

    public boolean isPrimary() {
        return primary;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public Method getAfterCreate() {
        return afterCreate;
    }

    public Method getBeforeDestroy() {
        return beforeDestroy;
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasAfterCreate() {
        return afterCreate != null;
    }

    public boolean hasBeforeDestroy() {
        return beforeDestroy != null;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", scope=" + scope +
                ", lazy=" + lazy +
                ", primary=" + primary +
                ", dependencies=" + dependencies +
                ", afterCreate=" + afterCreate +
                ", beforeDestroy=" + beforeDestroy +
                '}';
    }
}