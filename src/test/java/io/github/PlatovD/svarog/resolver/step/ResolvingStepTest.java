package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.exception.NoSuchBeanException;
import io.github.PlatovD.svarog.resolver.ResolutionContext;
import io.github.PlatovD.svarog.resolver.beans.Beans;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResolvingStepTest {

    private final IndexingStep indexing = new IndexingStep();
    private final ResolvingStep resolving = new ResolvingStep();

    private static Field field(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static BeanDefinition def(Class<?> type, Dependency... deps) {
        BeanDefinitionBuilder builder = new BeanDefinitionBuilder().type(type);
        for (Dependency dep : deps) {
            builder.addDependency(dep);
        }
        return builder.build();
    }

    private ResolutionContext run(List<BeanDefinition> defs) {
        ResolutionContext ctx = new ResolutionContext(defs);
        indexing.apply(ctx);
        resolving.apply(ctx);
        return ctx;
    }

    @Test
    void apply_beanWithoutDependencies_hasEmptyList() {
        BeanDefinition repo = def(Beans.Repository.class);

        ResolutionContext ctx = run(List.of(repo));
        Map<BeanDefinition, List<BeanDefinition>> resolved = ctx.getResolvedDependencies();

        assertTrue(resolved.containsKey(repo));
        assertTrue(resolved.get(repo).isEmpty());
    }

    @Test
    void apply_singleDependency_resolved() {
        BeanDefinition repo = def(Beans.Repository.class);
        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        ResolutionContext ctx = run(List.of(repo, service));
        Map<BeanDefinition, List<BeanDefinition>> resolved = ctx.getResolvedDependencies();

        assertEquals(1, resolved.get(service).size());
        assertSame(repo, resolved.get(service).get(0));
    }

    @Test
    void apply_allBeansAreKeys() {
        BeanDefinition repo = def(Beans.Repository.class);
        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        ResolutionContext ctx = run(List.of(repo, service));
        Map<BeanDefinition, List<BeanDefinition>> resolved = ctx.getResolvedDependencies();

        assertEquals(2, resolved.size());
        assertTrue(resolved.containsKey(repo));
        assertTrue(resolved.containsKey(service));
    }

    @Test
    void apply_chain_resolved() {
        BeanDefinition repo = def(Beans.Repository.class);
        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));
        BeanDefinition controller = def(Beans.Controller.class,
                new Dependency(Beans.Service.class, "",
                        field(Beans.Controller.class, "service")));

        ResolutionContext ctx = run(List.of(repo, service, controller));
        Map<BeanDefinition, List<BeanDefinition>> resolved = ctx.getResolvedDependencies();

        assertSame(service, resolved.get(controller).get(0));
        assertSame(repo, resolved.get(service).get(0));
        assertTrue(resolved.get(repo).isEmpty());
    }

    @Test
    void apply_missingDependency_throws() {
        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        assertThrows(NoSuchBeanException.class, () -> run(List.of(service)));
    }
}