package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.resolver.ResolutionContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SortingStep implements ResolutionStep {

    @Override
    public void apply(ResolutionContext context) {
        Map<BeanDefinition, List<BeanDefinition>> graph = context.getResolvedDependencies();

        Set<BeanDefinition> visited = new HashSet<>();
        List<BeanDefinition> order = new ArrayList<>();

        for (BeanDefinition def : context.getInput()) {
            if (!visited.contains(def)) {
                visit(def, graph, visited, order);
            }
        }

        context.setCreationOrder(order);
    }

    private void visit(
            BeanDefinition def,
            Map<BeanDefinition, List<BeanDefinition>> graph,
            Set<BeanDefinition> visited,
            List<BeanDefinition> order) {

        visited.add(def);

        for (BeanDefinition dependency : graph.getOrDefault(def, List.of())) {
            if (!visited.contains(dependency)) {
                visit(dependency, graph, visited, order);
            }
        }

        order.add(def);
    }
}