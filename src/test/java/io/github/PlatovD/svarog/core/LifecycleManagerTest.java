package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.core.testbeans.FailingLifecycleBean;
import io.github.PlatovD.svarog.core.testbeans.LifecycleBean;
import io.github.PlatovD.svarog.core.testbeans.PlainBean;
import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.exception.SvarogException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class LifecycleManagerTest {

    private final LifecycleManager manager = new LifecycleManager();

    private static Method method(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static BeanDefinition def(Class<?> type, Method afterCreate, Method beforeDestroy) {
        return new BeanDefinitionBuilder()
                .type(type)
                .afterCreate(afterCreate)
                .beforeDestroy(beforeDestroy)
                .build();
    }

    @Test
    void afterCreate_invokesMethod() {
        LifecycleBean bean = new LifecycleBean();
        BeanDefinition definition = def(
                LifecycleBean.class,
                method(LifecycleBean.class, "afterCreate"),
                null);

        manager.afterCreate(bean, definition);

        assertTrue(bean.afterCreateCalled);
    }

    @Test
    void beforeDestroy_invokesMethod() {
        LifecycleBean bean = new LifecycleBean();
        BeanDefinition definition = def(
                LifecycleBean.class,
                null,
                method(LifecycleBean.class, "beforeDestroy"));

        manager.beforeDestroy(bean, definition);

        assertTrue(bean.beforeDestroyCalled);
    }

    @Test
    void noLifecycleMethods_doesNothing() {
        PlainBean bean = new PlainBean();
        BeanDefinition definition = def(PlainBean.class, null, null);

        assertDoesNotThrow(() -> {
            manager.afterCreate(bean, definition);
            manager.beforeDestroy(bean, definition);
        });
    }

    @Test
    void afterCreate_methodThrows_wrapsInSvarogException() {
        FailingLifecycleBean bean = new FailingLifecycleBean();
        BeanDefinition definition = def(
                FailingLifecycleBean.class,
                method(FailingLifecycleBean.class, "afterCreate"),
                null);

        SvarogException ex = assertThrows(
                SvarogException.class,
                () -> manager.afterCreate(bean, definition));

        assertNotNull(ex.getCause());
        assertEquals("boom", ex.getCause().getMessage());
    }
}