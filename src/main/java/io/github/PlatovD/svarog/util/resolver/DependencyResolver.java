package io.github.PlatovD.svarog.util.resolver;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.exception.AmbiguousBeanException;
import io.github.PlatovD.svarog.exception.NoSuchBeanException;

import java.util.List;
import java.util.Map;

public final class DependencyResolver {

    private DependencyResolver() {
    }

    public static BeanDefinition resolve(
            Dependency dependency,
            Map<Class<?>, List<BeanDefinition>> byType,
            Map<String, BeanDefinition> byName) {

        if (dependency.hasQualifier()) {
            return resolveByQualifier(dependency, byName);
        }
        return resolveByType(dependency, byType);
    }

    private static BeanDefinition resolveByQualifier(
            Dependency dependency, Map<String, BeanDefinition> byName) {
        String qualifier = dependency.getQualifier();
        BeanDefinition found = byName.get(qualifier);
        if (found == null) {
            throw new NoSuchBeanException(
                    "No bean with name '" + qualifier + "' for dependency " +
                            dependency.getType().getName());
        }
        return found;
    }

    private static BeanDefinition resolveByType(
            Dependency dependency, Map<Class<?>, List<BeanDefinition>> byType) {
        List<BeanDefinition> candidates = byType.get(dependency.getType());
        if (candidates == null || candidates.isEmpty()) {
            throw new NoSuchBeanException(
                    "No bean of type " + dependency.getType().getName());
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        return pickPrimary(dependency, candidates);
    }

    private static BeanDefinition pickPrimary(
            Dependency dependency, List<BeanDefinition> candidates) {
        BeanDefinition primary = null;
        for (BeanDefinition candidate : candidates) {
            if (!candidate.isPrimary()) {
                continue;
            }
            if (primary != null) {
                throw new AmbiguousBeanException(
                        "Multiple @Primary beans of type " +
                                dependency.getType().getName());
            }
            primary = candidate;
        }
        if (primary == null) {
            throw new AmbiguousBeanException(
                    "Multiple candidates of type " +
                            dependency.getType().getName() +
                            ", none marked @Primary");
        }
        return primary;
    }
}