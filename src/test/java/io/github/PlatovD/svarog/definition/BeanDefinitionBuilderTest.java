package io.github.PlatovD.svarog.definition;

import io.github.PlatovD.svarog.exception.SvarogException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class BeanDefinitionBuilderTest {

    private static final Class<?> SERVICE = SampleBeans.Service.class;
    private static final Class<?> REPOSITORY = SampleBeans.Repository.class;

    private static Field field(String name) {
        try {
            return SampleBeans.Service.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void defaults() {
        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .build();

        assertEquals(SERVICE, def.getType());
        assertEquals("", def.getName());
        assertEquals(Scope.SINGLETON, def.getScope());
        assertFalse(def.isLazy());
        assertFalse(def.isPrimary());
        assertTrue(def.getDependencies().isEmpty());
        assertNull(def.getAfterCreate());
        assertNull(def.getBeforeDestroy());
    }

    @Test
    void setType() {
        BeanDefinition def = new BeanDefinitionBuilder()
                .type(REPOSITORY)
                .build();

        assertEquals(REPOSITORY, def.getType());
    }

    @Test
    void setName() {
        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .name("userService")
                .build();

        assertEquals("userService", def.getName());
        assertTrue(def.hasName());
    }

    @Test
    void setScope() {
        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .scope(Scope.PROTOTYPE)
                .build();

        assertEquals(Scope.PROTOTYPE, def.getScope());
    }

    @Test
    void setLazy() {
        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .lazy(true)
                .build();

        assertTrue(def.isLazy());
    }

    @Test
    void setPrimary() {
        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .primary(true)
                .build();

        assertTrue(def.isPrimary());
    }

    @Test
    void addDependency() {
        Dependency dep = new Dependency(REPOSITORY, "", field("repository"));

        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .addDependency(dep)
                .build();

        assertEquals(1, def.getDependencies().size());
        assertEquals(REPOSITORY, def.getDependencies().get(0).getType());
    }

    @Test
    void setAfterCreate() throws NoSuchMethodException {
        Method init = SampleBeans.Service.class.getDeclaredMethod("init");

        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .afterCreate(init)
                .build();

        assertEquals(init, def.getAfterCreate());
        assertTrue(def.hasAfterCreate());
    }

    @Test
    void setBeforeDestroy() throws NoSuchMethodException {
        Method close = SampleBeans.Service.class.getDeclaredMethod("close");

        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .beforeDestroy(close)
                .build();

        assertEquals(close, def.getBeforeDestroy());
        assertTrue(def.hasBeforeDestroy());
    }

    @Test
    void fluent_returnsSameBuilder() {
        BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
        assertSame(builder, builder.type(SERVICE));
        assertSame(builder, builder.name("x"));
        assertSame(builder, builder.scope(Scope.PROTOTYPE));
        assertSame(builder, builder.lazy(true));
        assertSame(builder, builder.primary(true));
        assertSame(builder, builder.addDependency(
                new Dependency(REPOSITORY, "", field("repository"))));
        assertSame(builder, builder.afterCreate(null));
        assertSame(builder, builder.beforeDestroy(null));
    }

    @Test
    void type_null_throws() {
        assertThrows(
                SvarogException.class,
                () -> new BeanDefinitionBuilder().type(null));
    }

    @Test
    void name_null_throws() {
        assertThrows(
                SvarogException.class,
                () -> new BeanDefinitionBuilder().name(null));
    }

    @Test
    void scope_null_throws() {
        assertThrows(
                SvarogException.class,
                () -> new BeanDefinitionBuilder().scope(null));
    }

    @Test
    void addDependency_null_throws() {
        assertThrows(
                SvarogException.class,
                () -> new BeanDefinitionBuilder().addDependency(null));
    }

    @Test
    void build_withoutType_throws() {
        assertThrows(
                SvarogException.class,
                () -> new BeanDefinitionBuilder().build());
    }

    @Test
    void dependencies_immutableAfterBuild() {
        Dependency dep = new Dependency(REPOSITORY, "", field("repository"));

        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .addDependency(dep)
                .build();

        assertThrows(
                UnsupportedOperationException.class,
                () -> def.getDependencies().add(dep));
    }

    @Test
    void fullConfiguration() throws NoSuchMethodException {
        Dependency dep1 = new Dependency(REPOSITORY, "mainRepo", field("repository"));
        Dependency dep2 = new Dependency(java.util.List.class, "items", field("items"));
        Method init = SampleBeans.Service.class.getDeclaredMethod("init");
        Method close = SampleBeans.Service.class.getDeclaredMethod("close");

        BeanDefinition def = new BeanDefinitionBuilder()
                .type(SERVICE)
                .name("userService")
                .scope(Scope.PROTOTYPE)
                .lazy(true)
                .primary(true)
                .addDependency(dep1)
                .addDependency(dep2)
                .afterCreate(init)
                .beforeDestroy(close)
                .build();

        assertEquals(SERVICE, def.getType());
        assertEquals("userService", def.getName());
        assertEquals(Scope.PROTOTYPE, def.getScope());
        assertTrue(def.isLazy());
        assertTrue(def.isPrimary());
        assertEquals(2, def.getDependencies().size());
        assertEquals(init, def.getAfterCreate());
        assertEquals(close, def.getBeforeDestroy());
    }
}