package io.github.PlatovD.svarog.definition;

import java.lang.reflect.Field;

public final class Dependency {

    private final Class<?> type;
    private final String qualifier;
    private final Field field;

    public Dependency(Class<?> type, String qualifier, Field field) {
        this.type = type;
        this.qualifier = qualifier;
        this.field = field;
    }

    public Class<?> getType() {
        return type;
    }

    public String getQualifier() {
        return qualifier;
    }

    public Field getField() {
        return field;
    }

    public boolean hasQualifier() {
        return qualifier != null && !qualifier.isEmpty();
    }
}
