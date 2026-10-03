package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.resolver.ResolutionContext;
import io.github.PlatovD.svarog.resolver.beans.Beans;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SortingStepTest {

    private final SortingStep step = new SortingStep();

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
    void apply_emptyGraph_emptyOrder() {
        ResolutionContext ctx = contextWith(new HashMap<>());
        step.apply(ctx);

        assertNotNull(ctx.getCreationOrder());
        assertTrue(ctx.getCreationOrder().isEmpty());
    }

    @Test
    void apply_singleBean_orderHasOne() {
        BeanDefinition a = def(Beans.Repository.class);
        ResolutionContext ctx = contextWith(graph(a, List.of()));
        step.apply(ctx);

        assertEquals(List.of(a), ctx.getCreationOrder());
    }

    @Test
    void apply_chain_dependenciesFirst() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);
        BeanDefinition c = def(Beans.Controller.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b),
                b, List.of(c),
                c, List.of()
        ));
        step.apply(ctx);

        List<BeanDefinition> order = ctx.getCreationOrder();
        assertEquals(3, order.size());
        assertTrue(order.indexOf(c) < order.indexOf(b));
        assertTrue(order.indexOf(b) < order.indexOf(a));
    }

    @Test
    void apply_twoIndependentBeans_bothPresent() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(),
                b, List.of()
        ));
        step.apply(ctx);

        List<BeanDefinition> order = ctx.getCreationOrder();
        assertEquals(2, order.size());
        assertTrue(order.contains(a));
        assertTrue(order.contains(b));
    }

    @Test
    void apply_branching_dependenciesBeforeDependent() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);
        BeanDefinition c = def(Beans.Controller.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b, c),
                b, List.of(),
                c, List.of()
        ));
        step.apply(ctx);

        List<BeanDefinition> order = ctx.getCreationOrder();
        assertEquals(3, order.size());
        assertTrue(order.indexOf(b) < order.indexOf(a));
        assertTrue(order.indexOf(c) < order.indexOf(a));
    }

    @Test
    void apply_allBeansInOrder() {
        BeanDefinition a = def(Beans.Repository.class);
        BeanDefinition b = def(Beans.Service.class);
        BeanDefinition c = def(Beans.Controller.class);

        ResolutionContext ctx = contextWith(graph(
                a, List.of(b),
                b, List.of(c),
                c, List.of()
        ));
        step.apply(ctx);

        List<BeanDefinition> order = ctx.getCreationOrder();
        assertEquals(3, order.size());
        assertTrue(order.contains(a));
        assertTrue(order.contains(b));
        assertTrue(order.contains(c));
    }
}
