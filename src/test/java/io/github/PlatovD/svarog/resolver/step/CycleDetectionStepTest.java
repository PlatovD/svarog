package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.exception.CircularDependencyException;
import io.github.PlatovD.svarog.resolver.ResolutionContext;
import io.github.PlatovD.svarog.resolver.beans.Beans;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CycleDetectionStepTest {

    private final CycleDetectionStep step = new CycleDetectionStep();

    private static BeanDefinition def(Class<?> type) {
        return new BeanDefinitionBuilder().type(type).build();
    }

    private static ResolutionContext contextWith(
            Map<BeanDefinition, List<BeanDefinition>> graph) {

        List<BeanDefinition> input = new ArrayList<>(graph.keySet());
        ResolutionContext ctx = new ResolutionContext(input);
        ctx.setResolvedDependencies(graph);
        return ctx;
    }

    private static Map<BeanDefinition, List<BeanDefinition>> graph(Object... pairs) {
        Map<BeanDefinition, List<BeanDefinition>> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            BeanDefinition key = (BeanDefinition) pairs[i];
            @SuppressWarnings("unchecked")
            List<BeanDefinition> values = (List<BeanDefinition>) pairs[i + 1];
            map.put(key, values);
        }
        return map;
    }

    @Test
    void apply_emptyGraph_noCycle() {
        ResolutionContext ctx = contextWith(new HashMap<>());
        assertDoesNotThrow(() -> step.apply(ctx));
    }

    @Test
    void apply_singleBean_noCycle() {
        BeanDefinition a = def(Beans.Repository.class);
        ResolutionContext ctx = contextWith(graph(a, List.of()));
        assertDoesNotThrow(() -> step.apply(ctx));
    }

    @Test
    void apply_chain_noCycle() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);
        BeanDefinition c = def(Beans.Controller.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b),
                b, List.of(c),
                c, List.of()
        ));

        assertDoesNotThrow(() -> step.apply(ctx));
    }

    @Test
    void apply_selfCycle_throws() {
        BeanDefinition a = def(Beans.Repository.class);
        ResolutionContext ctx = contextWith(graph(a, List.of(a)));

        CircularDependencyException ex = assertThrows(
                CircularDependencyException.class,
                () -> step.apply(ctx));

        assertTrue(ex.getMessage().contains("Repository"));
    }

    @Test
    void apply_twoNodeCycle_throws() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b),
                b, List.of(a)
        ));

        assertThrows(CircularDependencyException.class, () -> step.apply(ctx));
    }

    @Test
    void apply_threeNodeCycle_throws() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);
        BeanDefinition c = def(Beans.Controller.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b),
                b, List.of(c),
                c, List.of(a)
        ));

        assertThrows(CircularDependencyException.class, () -> step.apply(ctx));
    }

    @Test
    void apply_cycleInBranch_throws() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);
        BeanDefinition c = def(Beans.Controller.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b),
                b, List.of(c),
                c, List.of(b)
        ));

        assertThrows(CircularDependencyException.class, () -> step.apply(ctx));
    }
}