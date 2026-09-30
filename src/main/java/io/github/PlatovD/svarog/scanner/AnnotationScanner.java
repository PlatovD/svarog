package io.github.PlatovD.svarog.scanner;

import io.github.PlatovD.svarog.annotation.*;
import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.exception.SvarogScannerException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class AnnotationScanner {
    public List<BeanDefinition> scan(Set<Class<?>> classes) {
        if (classes == null) {
            throw new SvarogScannerException("classes must not be null");
        }

        List<BeanDefinition> definitions = new ArrayList<>();

        for (Class<?> clazz : classes) {
            AutoCreate autoCreate = clazz.getAnnotation(AutoCreate.class);
            if (autoCreate == null) {
                continue;
            }
            definitions.add(buildDefinition(clazz, autoCreate));
        }

        return definitions;
    }

    private BeanDefinition buildDefinition(Class<?> clazz, AutoCreate autoCreate) {
        BeanDefinitionBuilder builder = new BeanDefinitionBuilder()
                .type(clazz)
                .scope(autoCreate.scope())
                .lazy(autoCreate.lazy())
                .name(autoCreate.name());

        if (clazz.isAnnotationPresent(Primary.class)) {
            builder.primary(true);
        }

        scanFields(clazz, builder);
        scanMethods(clazz, builder);

        return builder.build();
    }

    private void scanFields(Class<?> clazz, BeanDefinitionBuilder builder) {
        for (Field field : clazz.getDeclaredFields()) {
            AutoInject autoInject = field.getAnnotation(AutoInject.class);
            if (autoInject == null) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            Dependency dependency = new Dependency(field.getType(), autoInject.qualifier(), field);
            builder.addDependency(dependency);
        }
    }

    private void scanMethods(Class<?> clazz, BeanDefinitionBuilder builder) {
        Method afterCreate = null;
        Method beforeDestroy = null;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterCreate.class)) {
                validateLifecycleMethod(method, AfterCreate.class);
                if (afterCreate != null) {
                    throw new SvarogScannerException("Class " + clazz.getName() + " has more than one @AfterCreate method");
                }
                afterCreate = method;
            }

            if (method.isAnnotationPresent(BeforeDestroy.class)) {
                validateLifecycleMethod(method, BeforeDestroy.class);
                if (beforeDestroy != null) {
                    throw new SvarogScannerException("Class " + clazz.getName() + " has more than one @BeforeDestroy method");
                }
                beforeDestroy = method;
            }
        }

        builder.afterCreate(afterCreate);
        builder.beforeDestroy(beforeDestroy);
    }

    private void validateLifecycleMethod(Method method, Class<?> annotation) {
        if (Modifier.isStatic(method.getModifiers())) {
            throw new SvarogScannerException("@" + annotation.getSimpleName() + " method must not be static: " + method);
        }
        if (method.getParameterCount() != 0) {
            throw new SvarogScannerException("@" + annotation.getSimpleName() + " method must not have parameters: " + method);
        }
    }
}
