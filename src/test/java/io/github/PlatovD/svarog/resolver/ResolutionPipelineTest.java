package io.github.PlatovD.svarog.resolver;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.exception.AmbiguousBeanException;
import io.github.PlatovD.svarog.exception.CircularDependencyException;
import io.github.PlatovD.svarog.exception.NoSuchBeanException;
import io.github.PlatovD.svarog.resolver.beans.Beans;
import io.github.PlatovD.svarog.resolver.step.IndexingStep;
import io.github.PlatovD.svarog.resolver.step.ResolvingStep;
import io.github.PlatovD.svarog.resolver.step.SortingStep;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResolutionPipelineTest {

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

    private static BeanDefinition named(Class<?> type, String name) {
        return new BeanDefinitionBuilder().type(type).name(name).build();
    }

    private static BeanDefinition primary(Class<?> type) {
        return new BeanDefinitionBuilder().type(type).primary(true).build();
    }

    @Test
    void resolve_emptyInput_emptyResult() {
        ResolutionResult result = ResolutionPipeline.defaultPipeline().resolve(List.of());

        assertNotNull(result);
        assertTrue(result.getCreationOrder().isEmpty());
        assertTrue(result.getDependencies().isEmpty());
        assertTrue(result.getByType().isEmpty());
        assertTrue(result.getByName().isEmpty());
    }

    @Test
    void resolve_singleBean_orderHasOne() {
        BeanDefinition repo = def(Beans.Repository.class);

        ResolutionResult result = ResolutionPipeline.defaultPipeline().resolve(List.of(repo));

        assertEquals(1, result.getCreationOrder().size());
        assertSame(repo, result.getCreationOrder().get(0));
    }

    @Test
    void resolve_chain_correctOrderAndGraph() {
        BeanDefinition repo = def(Beans.Repository.class);
        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));
        BeanDefinition controller = def(Beans.Controller.class,
                new Dependency(Beans.Service.class, "",
                        field(Beans.Controller.class, "service")));

        ResolutionResult result = ResolutionPipeline.defaultPipeline()
                .resolve(List.of(controller, service, repo));

        List<BeanDefinition> order = result.getCreationOrder();
        assertEquals(3, order.size());
        assertTrue(order.indexOf(repo) < order.indexOf(service));
        assertTrue(order.indexOf(service) < order.indexOf(controller));

        assertSame(repo, result.getDependencies().get(service).get(0));
        assertSame(service, result.getDependencies().get(controller).get(0));

        assertEquals(1, result.getByType().get(Beans.Repository.class).size());
        assertEquals(1, result.getByType().get(Beans.Service.class).size());
    }

    @Test
    void resolve_twoBeans_bothInOrder() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        ResolutionResult result = ResolutionPipeline.defaultPipeline()
                .resolve(List.of(a, b));

        assertEquals(2, result.getCreationOrder().size());
        assertTrue(result.getCreationOrder().indexOf(a) < result.getCreationOrder().indexOf(b));
    }

    @Test
    void resolve_duplicateName_throws() {
        BeanDefinition a = named(Beans.Repository.class, "dup");
        BeanDefinition b = named(Beans.Service.class, "dup");

        assertThrows(AmbiguousBeanException.class, () ->
                ResolutionPipeline.defaultPipeline().resolve(List.of(a, b)));
    }

    @Test
    void resolve_missingDependency_throws() {
        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        assertThrows(NoSuchBeanException.class, () ->
                ResolutionPipeline.defaultPipeline().resolve(List.of(service)));
    }

    @Test
    void resolve_cycle_throws() {
        BeanDefinition a = def(Beans.Repository.class,
                new Dependency(Beans.Service.class, "",
                        field(Beans.Service.class, "repository")));
        BeanDefinition b = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        assertThrows(CircularDependencyException.class, () ->
                ResolutionPipeline.defaultPipeline().resolve(List.of(a, b)));
    }

    @Test
    void resolve_primaryChosenOnConflict() {
        BeanDefinition main = named(Beans.Repository.class, "mainRepo");
        BeanDefinition backup = primary(Beans.Repository.class);

        BeanDefinition service = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        ResolutionResult result = ResolutionPipeline.defaultPipeline()
                .resolve(List.of(main, backup, service));

        assertSame(backup, result.getDependencies().get(service).get(0));
    }

    @Test
    void resolve_customPipeline_withoutCycleDetection_doesNotThrowOnCycle() {
        BeanDefinition a = def(Beans.Repository.class,
                new Dependency(Beans.Service.class, "",
                        field(Beans.Service.class, "repository")));
        BeanDefinition b = def(Beans.Service.class,
                new Dependency(Beans.Repository.class, "",
                        field(Beans.Service.class, "repository")));

        ResolutionPipeline custom = ResolutionPipeline.builder()
                .addStep(new IndexingStep())
                .addStep(new ResolvingStep())
                .addStep(new SortingStep())
                .build();

        assertDoesNotThrow(() -> custom.resolve(List.of(a, b)));
    }
}