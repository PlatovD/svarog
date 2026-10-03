package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.exception.CircularDependencyException;
import io.github.PlatovD.svarog.resolver.ResolutionContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CycleDetectionStep implements ResolutionStep {

    private enum Color {
        WHITE, GRAY, BLACK
    }

    @Override
    public void apply(ResolutionContext context) {
        Map<BeanDefinition, List<BeanDefinition>> graph = context.getResolvedDependencies();
        Map<BeanDefinition, Color> colors = new HashMap<>();

        for (BeanDefinition def : context.getInput()) {
            colors.put(def, Color.WHITE);
        }

        Deque<BeanDefinition> path = new ArrayDeque<>();

        for (BeanDefinition def : context.getInput()) {
            if (colors.get(def) == Color.WHITE) {
                visit(def, graph, colors, path);
            }
        }
    }

    private void visit(
            BeanDefinition def,
            Map<BeanDefinition, List<BeanDefinition>> graph,
            Map<BeanDefinition, Color> colors,
            Deque<BeanDefinition> path) {

        colors.put(def, Color.GRAY);
        path.addLast(def);

        List<BeanDefinition> neighbours = graph.getOrDefault(def, List.of());
        for (BeanDefinition neighbour : neighbours) {
            Color color = colors.get(neighbour);
            if (color == Color.GRAY) {
                throw buildCycleException(path, neighbour);
            }
            if (color == Color.WHITE) {
                visit(neighbour, graph, colors, path);
            }
        }

        path.removeLast();
        colors.put(def, Color.BLACK);
    }

    private CircularDependencyException buildCycleException(
            Deque<BeanDefinition> path, BeanDefinition cycleStart) {

        StringBuilder sb = new StringBuilder("Circular dependency detected: ");
        boolean started = false;
        for (BeanDefinition def : path) {
            if (def.equals(cycleStart)) {
                started = true;
            }
            if (started) {
                sb.append(def.getType().getSimpleName()).append(" -> ");
            }
        }
        sb.append(cycleStart.getType().getSimpleName());
        return new CircularDependencyException(sb.toString());
    }
}