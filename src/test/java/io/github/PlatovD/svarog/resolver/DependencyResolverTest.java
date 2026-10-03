package io.github.PlatovD.svarog.resolver;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.exception.AmbiguousBeanException;
import io.github.PlatovD.svarog.exception.NoSuchBeanException;
import io.github.PlatovD.svarog.resolver.beans.Beans;
import io.github.PlatovD.svarog.util.resolver.DependencyResolver;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DependencyResolverTest {

    private static Field field(String name) {
        try {
            return Beans.Service.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static BeanDefinition def(Class<?> type) {
        return new BeanDefinitionBuilder().type(type).build();
    }

    private static BeanDefinition named(Class<?> type, String name) {
        return new BeanDefinitionBuilder().type(type).name(name).build();
    }

    private static BeanDefinition primary(Class<?> type) {
        return new BeanDefinitionBuilder().type(type).primary(true).build();
    }

    @Test
    void resolve_byType_single() {
        BeanDefinition repo = def(Beans.Repository.class);

        BeanDefinition result = DependencyResolver.resolve(
                new Dependency(Beans.Repository.class, "", field("repository")),
                Map.of(Beans.Repository.class, List.of(repo)),
                Map.of());

        assertSame(repo, result);
    }

    @Test
    void resolve_byType_notFound_throws() {
        assertThrows(NoSuchBeanException.class, () ->
                DependencyResolver.resolve(
                        new Dependency(Beans.Repository.class, "", field("repository")),
                        Map.of(),
                        Map.of()));
    }

    @Test
    void resolve_byType_ambiguous_throws() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Repository.class);

        assertThrows(AmbiguousBeanException.class, () ->
                DependencyResolver.resolve(
                        new Dependency(Beans.Repository.class, "", field("repository")),
                        Map.of(Beans.Repository.class, List.of(a, b)),
                        Map.of()));
    }

    @Test
    void resolve_byType_primary_wins() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = primary(Beans.Repository.class);

        BeanDefinition result = DependencyResolver.resolve(
                new Dependency(Beans.Repository.class, "", field("repository")),
                Map.of(Beans.Repository.class, List.of(a, b)),
                Map.of());

        assertSame(b, result);
    }

    @Test
    void resolve_byType_multiplePrimary_throws() {
        BeanDefinition a = primary(Beans.Repository.class);
        BeanDefinition b = primary(Beans.Repository.class);

        assertThrows(AmbiguousBeanException.class, () ->
                DependencyResolver.resolve(
                        new Dependency(Beans.Repository.class, "", field("repository")),
                        Map.of(Beans.Repository.class, List.of(a, b)),
                        Map.of()));
    }

    @Test
    void resolve_byQualifier() {
        BeanDefinition repo = named(Beans.Repository.class, "mainRepo");

        BeanDefinition result = DependencyResolver.resolve(
                new Dependency(Beans.Repository.class, "mainRepo", field("repository")),
                Map.of(),
                Map.of("mainRepo", repo));

        assertSame(repo, result);
    }

    @Test
    void resolve_byQualifier_notFound_throws() {
        assertThrows(NoSuchBeanException.class, () ->
                DependencyResolver.resolve(
                        new Dependency(Beans.Repository.class, "missing", field("repository")),
                        Map.of(),
                        Map.of()));
    }
}