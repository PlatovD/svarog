package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.core.testbeans.InjectTarget;
import io.github.PlatovD.svarog.core.testbeans.NamedBean;
import io.github.PlatovD.svarog.core.testbeans.SimpleBean;
import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.definition.Dependency;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class FieldInjectorTest {

    private final FieldInjector injector = new FieldInjector();

    private static Field field(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void inject_byType_setsField() {
        SimpleBean simple = new SimpleBean();
        BeanProvider provider = new TestBeanProvider().put(SimpleBean.class, simple);

        InjectTarget target = new InjectTarget();
        BeanDefinition definition = new BeanDefinitionBuilder()
                .type(InjectTarget.class)
                .addDependency(new Dependency(
                        SimpleBean.class, "", field(InjectTarget.class, "simpleBean")))
                .build();

        injector.inject(target, definition, provider);

        assertSame(simple, field(InjectTarget.class, "simpleBean").trySetAccessible() ?
                readField(target, "simpleBean") : null);
    }

    @Test
    void inject_byQualifier_setsField() {
        NamedBean named = new NamedBean();
        BeanProvider provider = new TestBeanProvider().put("customName", named);

        InjectTarget target = new InjectTarget();
        BeanDefinition definition = new BeanDefinitionBuilder()
                .type(InjectTarget.class)
                .addDependency(new Dependency(
                        NamedBean.class, "customName",
                        field(InjectTarget.class, "namedBean")))
                .build();

        injector.inject(target, definition, provider);

        assertSame(named, readField(target, "namedBean"));
    }

    @Test
    void inject_noDependencies_doesNothing() {
        InjectTarget target = new InjectTarget();
        BeanDefinition definition = new BeanDefinitionBuilder()
                .type(InjectTarget.class)
                .build();

        assertDoesNotThrow(() -> injector.inject(target, definition, new TestBeanProvider()));

        assertNull(readField(target, "simpleBean"));
        assertNull(readField(target, "namedBean"));
    }

    private static Object readField(Object target, String name) {
        try {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}