package io.github.PlatovD.svarog.definition;

import io.github.PlatovD.svarog.exception.SvarogException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class BeanDefinitionBuilder {

    private Class<?> type;
    private String name = "";
    private Scope scope = Scope.SINGLETON;
    private boolean lazy = false;
    private boolean primary = false;
    private final List<Dependency> dependencies = new ArrayList<>();
    private Method afterCreate;
    private Method beforeDestroy;

    public BeanDefinitionBuilder type(Class<?> type) {
        if (type == null) {
            throw new SvarogException("type must not be null");
        }
        this.type = type;
        return this;
    }

    public BeanDefinitionBuilder name(String name) {
        if (name == null) {
            throw new SvarogException("name must not be null");
        }
        this.name = name;
        return this;
    }

    public BeanDefinitionBuilder scope(Scope scope) {
        if (scope == null) {
            throw new SvarogException("scope must not be null");
        }
        this.scope = scope;
        return this;
    }

    public BeanDefinitionBuilder lazy(boolean lazy) {
        this.lazy = lazy;
        return this;
    }

    public BeanDefinitionBuilder primary(boolean primary) {
        this.primary = primary;
        return this;
    }

    public BeanDefinitionBuilder addDependency(Dependency dependency) {
        if (dependency == null) {
            throw new SvarogException("dependency must not be null");
        }
        this.dependencies.add(dependency);
        return this;
    }

    public BeanDefinitionBuilder afterCreate(Method afterCreate) {
        this.afterCreate = afterCreate;
        return this;
    }

    public BeanDefinitionBuilder beforeDestroy(Method beforeDestroy) {
        this.beforeDestroy = beforeDestroy;
        return this;
    }

    public BeanDefinition build() {
        if (type == null) {
            throw new SvarogException("type is required");
        }
        return new BeanDefinition(
                type,
                name,
                scope,
                lazy,
                primary,
                List.copyOf(dependencies),
                afterCreate,
                beforeDestroy
        );
    }
}
