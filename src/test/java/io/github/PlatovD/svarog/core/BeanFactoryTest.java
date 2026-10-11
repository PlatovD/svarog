package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.core.testbeans.AbstractBean;
import io.github.PlatovD.svarog.core.testbeans.NoDefaultConstructorBean;
import io.github.PlatovD.svarog.core.testbeans.SimpleBean;
import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.exception.SvarogException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {

    private final BeanFactory factory = new BeanFactory();

    private static BeanDefinition def(Class<?> type) {
        return new BeanDefinitionBuilder().type(type).build();
    }

    @Test
    void create_simpleBean_returnsInstance() {
        Object instance = factory.create(def(SimpleBean.class));

        assertNotNull(instance);
        assertInstanceOf(SimpleBean.class, instance);
    }

    @Test
    void create_simpleBean_fieldsAreDefault() {
        SimpleBean instance = (SimpleBean) factory.create(def(SimpleBean.class));

        assertEquals("default", instance.value);
    }

    @Test
    void create_noDefaultConstructor_throws() {
        assertThrows(SvarogException.class,
                () -> factory.create(def(NoDefaultConstructorBean.class)));
    }

    @Test
    void create_abstractClass_throws() {
        assertThrows(SvarogException.class,
                () -> factory.create(def(AbstractBean.class)));
    }
}