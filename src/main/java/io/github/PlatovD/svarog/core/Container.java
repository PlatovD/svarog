package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.config.SvarogConfig;
import io.github.PlatovD.svarog.config.loader.DefaultConfigLoader;
import io.github.PlatovD.svarog.config.factory.ConfigFactory;
import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogException;
import io.github.PlatovD.svarog.resolver.ResolutionPipeline;
import io.github.PlatovD.svarog.resolver.ResolutionResult;
import io.github.PlatovD.svarog.scanner.AnnotationScanner;
import io.github.PlatovD.svarog.scanner.ClassScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Container implements AutoCloseable, BeanProvider {

    private final SvarogConfig config;
    private final ResolutionResult resolution;
    private final BeanFactory beanFactory;
    private final FieldInjector fieldInjector;
    private final LifecycleManager lifecycleManager;

    private final Map<Class<?>, Object> singletonByType = new HashMap<>();
    private final Map<String, Object> singletonByName = new HashMap<>();

    Container(ConfigFactory configFactory, ResolutionPipeline pipeline) {
        this.config = new DefaultConfigLoader(configFactory).load();
        this.beanFactory = new BeanFactory();
        this.fieldInjector = new FieldInjector();
        this.lifecycleManager = new LifecycleManager();
        this.resolution = resolveDefinitions(pipeline);
        initializeEagerBeans();
    }

    public static Container create() {
        return builder().build();
    }

    public static ContainerBuilder builder() {
        return new ContainerBuilder();
    }

    @Override
    public <T> T getBean(Class<T> type) {
        if (type == null) {
            throw new SvarogException("type must not be null");
        }

        BeanDefinition definition = findDefinitionByType(type);
        if (definition.getScope() == Scope.SINGLETON) {
            Object cached = singletonByType.get(type);
            if (cached != null) {
                return type.cast(cached);
            }
        }

        Object instance = createBean(definition);
        if (definition.getScope() == Scope.SINGLETON) {
            singletonByType.put(type, instance);
        }
        return type.cast(instance);
    }

    @Override
    public Object getBean(String name) {
        if (name == null) {
            throw new SvarogException("name must not be null");
        }

        BeanDefinition definition = resolution.getByName().get(name);
        if (definition == null) {
            throw new SvarogException("No bean with name '" + name + "'");
        }

        if (definition.getScope() == Scope.SINGLETON) {
            Object cached = singletonByName.get(name);
            if (cached != null) {
                return cached;
            }
        }
        Object instance = createBean(definition);
        if (definition.getScope() == Scope.SINGLETON) {
            singletonByName.put(name, instance);
        }
        return instance;
    }

    public <T> List<T> getBeansOfType(Class<T> type) {
        if (type == null) {
            throw new SvarogException("type must not be null");
        }

        List<BeanDefinition> definitions = resolution.getByType().get(type);
        if (definitions == null || definitions.isEmpty()) {
            return List.of();
        }

        List<T> result = new ArrayList<>(definitions.size());
        for (BeanDefinition definition : definitions) {
            Object instance;
            if (definition.getScope() == Scope.SINGLETON) {
                instance = singletonByType.get(definition.getType());
                if (instance == null) {
                    instance = createBean(definition);
                    singletonByType.put(definition.getType(), instance);
                }
            } else {
                instance = createBean(definition);
            }
            result.add(type.cast(instance));
        }
        return result;
    }

    public boolean containsBean(Class<?> type) {
        return resolution.getByType().containsKey(type);
    }

    public boolean containsBean(String name) {
        return resolution.getByName().containsKey(name);
    }

    @Override
    public void close() {
        List<BeanDefinition> order = resolution.getCreationOrder();
        for (int i = order.size() - 1; i >= 0; i--) {
            BeanDefinition definition = order.get(i);
            Object instance = singletonByType.get(definition.getType());
            if (instance == null) {
                continue;
            }
            lifecycleManager.beforeDestroy(instance, definition);
        }
        singletonByType.clear();
        singletonByName.clear();
    }

    private ResolutionResult resolveDefinitions(ResolutionPipeline pipeline) {
        Set<Class<?>> classes = new ClassScanner()
                .scan(config.getScanPackage(), config.isScanRecursive());

        List<BeanDefinition> definitions = new AnnotationScanner().scan(classes);

        return pipeline.resolve(definitions);
    }

    private void initializeEagerBeans() {
        if (config.isLazyInit()) {
            return;
        }
        for (BeanDefinition definition : resolution.getCreationOrder()) {
            if (definition.isLazy()) {
                continue;
            }
            getBean(definition.getType());
        }
    }

    private Object createBean(BeanDefinition definition) {
        Object instance = beanFactory.create(definition);
        fieldInjector.inject(instance, definition, this);
        lifecycleManager.afterCreate(instance, definition);
        return instance;
    }

    private BeanDefinition findDefinitionByType(Class<?> type) {
        List<BeanDefinition> candidates = resolution.getByType().get(type);
        if (candidates == null || candidates.isEmpty()) {
            throw new SvarogException("No bean of type " + type.getName());
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        return pickPrimary(type, candidates);
    }

    private BeanDefinition pickPrimary(Class<?> type, List<BeanDefinition> candidates) {
        BeanDefinition primary = null;
        for (BeanDefinition candidate : candidates) {
            if (!candidate.isPrimary()) {
                continue;
            }
            if (primary != null) {
                throw new SvarogException(
                        "Multiple @Primary beans of type " + type.getName());
            }
            primary = candidate;
        }
        if (primary == null) {
            throw new SvarogException(
                    "Multiple candidates of type " + type.getName() +
                            ", none marked @Primary");
        }
        return primary;
    }
}