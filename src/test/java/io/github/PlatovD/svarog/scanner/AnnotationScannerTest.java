package io.github.PlatovD.svarog.scanner;

import io.github.PlatovD.svarog.scanner.annotated.*;
import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.Dependency;
import io.github.PlatovD.svarog.definition.Scope;
import io.github.PlatovD.svarog.exception.SvarogScannerException;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationScannerTest {

    private final AnnotationScanner scanner = new AnnotationScanner();

    private static Set<Class<?>> allClasses() {
        return Set.of(
                PlainService.class,
                SimpleService.class,
                NamedService.class,
                PrimaryService.class,
                InjectedService.class,
                LifecycleService.class
        );
    }

    private static BeanDefinition find(List<BeanDefinition> defs, Class<?> type) {
        return defs.stream()
                .filter(d -> d.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Not found: " + type));
    }

    @Test
    void scan_skipsClassesWithoutAutoCreate() {
        List<BeanDefinition> defs = scanner.scan(allClasses());

        assertFalse(defs.stream()
                .anyMatch(d -> d.getType().equals(PlainService.class)));
    }

    @Test
    void scan_findsAnnotatedClasses() {
        List<BeanDefinition> defs = scanner.scan(allClasses());

        assertTrue(defs.stream()
                .anyMatch(d -> d.getType().equals(SimpleService.class)));
        assertTrue(defs.stream()
                .anyMatch(d -> d.getType().equals(NamedService.class)));
    }

    @Test
    void scan_defaults() {
        List<BeanDefinition> defs = scanner.scan(Set.of(SimpleService.class));
        BeanDefinition def = find(defs, SimpleService.class);

        assertEquals(Scope.SINGLETON, def.getScope());
        assertFalse(def.isLazy());
        assertFalse(def.isPrimary());
        assertFalse(def.hasName());
        assertTrue(def.getDependencies().isEmpty());
    }

    @Test
    void scan_customName() {
        List<BeanDefinition> defs = scanner.scan(Set.of(NamedService.class));
        BeanDefinition def = find(defs, NamedService.class);

        assertEquals("customName", def.getName());
        assertTrue(def.hasName());
    }

    @Test
    void scan_customScope() {
        List<BeanDefinition> defs = scanner.scan(Set.of(NamedService.class));
        BeanDefinition def = find(defs, NamedService.class);

        assertEquals(Scope.PROTOTYPE, def.getScope());
    }

    @Test
    void scan_customLazy() {
        List<BeanDefinition> defs = scanner.scan(Set.of(NamedService.class));
        BeanDefinition def = find(defs, NamedService.class);

        assertTrue(def.isLazy());
    }

    @Test
    void scan_primary() {
        List<BeanDefinition> defs = scanner.scan(Set.of(PrimaryService.class));
        BeanDefinition def = find(defs, PrimaryService.class);

        assertTrue(def.isPrimary());
    }

    @Test
    void scan_dependencies_size() {
        List<BeanDefinition> defs = scanner.scan(Set.of(InjectedService.class));
        BeanDefinition def = find(defs, InjectedService.class);

        assertEquals(2, def.getDependencies().size());
    }

    @Test
    void scan_dependencyType() {
        List<BeanDefinition> defs = scanner.scan(Set.of(InjectedService.class));
        BeanDefinition def = find(defs, InjectedService.class);

        Dependency simple = def.getDependencies().stream()
                .filter(d -> d.getType().equals(SimpleService.class))
                .findFirst()
                .orElseThrow();

        assertEquals(SimpleService.class, simple.getType());
        assertFalse(simple.hasQualifier());
    }

    @Test
    void scan_dependencyQualifier() {
        List<BeanDefinition> defs = scanner.scan(Set.of(InjectedService.class));
        BeanDefinition def = find(defs, InjectedService.class);

        Dependency named = def.getDependencies().stream()
                .filter(d -> d.getType().equals(NamedService.class))
                .findFirst()
                .orElseThrow();

        assertEquals("customName", named.getQualifier());
        assertTrue(named.hasQualifier());
    }

    @Test
    void scan_skipsNonAnnotatedFields() {
        List<BeanDefinition> defs = scanner.scan(Set.of(InjectedService.class));
        BeanDefinition def = find(defs, InjectedService.class);

        assertFalse(def.getDependencies().stream()
                .anyMatch(d -> d.getType().equals(String.class)));
    }

    @Test
    void scan_afterCreate() {
        List<BeanDefinition> defs = scanner.scan(Set.of(LifecycleService.class));
        BeanDefinition def = find(defs, LifecycleService.class);

        assertTrue(def.hasAfterCreate());
        assertEquals("init", def.getAfterCreate().getName());
    }

    @Test
    void scan_beforeDestroy() {
        List<BeanDefinition> defs = scanner.scan(Set.of(LifecycleService.class));
        BeanDefinition def = find(defs, LifecycleService.class);

        assertTrue(def.hasBeforeDestroy());
        assertEquals("close", def.getBeforeDestroy().getName());
    }

    @Test
    void scan_duplicateAfterCreate_throws() {
        assertThrows(
                SvarogScannerException.class,
                () -> scanner.scan(Set.of(DuplicateAfterCreate.class)));
    }

    @Test
    void scan_staticLifecycleMethod_throws() {
        assertThrows(
                SvarogScannerException.class,
                () -> scanner.scan(Set.of(StaticLifecycleMethod.class)));
    }

    @Test
    void scan_paramLifecycleMethod_throws() {
        assertThrows(
                SvarogScannerException.class,
                () -> scanner.scan(Set.of(ParamLifecycleMethod.class)));
    }

    @Test
    void scan_nullInput_throws() {
        assertThrows(
                SvarogScannerException.class,
                () -> scanner.scan(null));
    }

    @Test
    void scan_emptyInput_returnsEmpty() {
        List<BeanDefinition> defs = scanner.scan(Set.of());

        assertNotNull(defs);
        assertTrue(defs.isEmpty());
    }
}