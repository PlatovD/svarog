package io.github.PlatovD.svarog.scanner;

import io.github.PlatovD.svarog.exception.SvarogScannerException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassScannerTest {

    private static final String PKG = "io.github.PlatovD.svarog.scanner.testclasses";

    private final ClassScanner scanner = new ClassScanner();

    @Test
    void scan_findsClassesInPackage() {
        Set<Class<?>> classes = scanner.scan(PKG, false);

        assertTrue(classes.stream().anyMatch(c -> c.getSimpleName().equals("SimpleClass")));
        assertTrue(classes.stream().anyMatch(c -> c.getSimpleName().equals("AnotherClass")));
    }

    @Test
    void scan_skipsInterfaces() {
        Set<Class<?>> classes = scanner.scan(PKG, false);

        assertFalse(classes.stream().anyMatch(c -> c.getSimpleName().equals("TestInterface")));
    }

    @Test
    void scan_skipsAbstractClasses() {
        Set<Class<?>> classes = scanner.scan(PKG, false);

        assertFalse(classes.stream().anyMatch(c -> c.getSimpleName().equals("AbstractClass")));
    }

    @Test
    void scan_skipsInnerNonStatic() {
        Set<Class<?>> classes = scanner.scan(PKG, false);

        assertFalse(classes.stream().anyMatch(c -> c.getSimpleName().equals("Inner")));
    }

    @Test
    void scan_keepsStaticNested() {
        Set<Class<?>> classes = scanner.scan(PKG, false);

        assertTrue(classes.stream().anyMatch(c -> c.getSimpleName().equals("StaticNested")));
    }

    @Test
    void scan_notRecursive_doesNotFindSubPackage() {
        Set<Class<?>> classes = scanner.scan(PKG, false);

        assertFalse(classes.stream().anyMatch(c -> c.getSimpleName().equals("SubClass")));
    }

    @Test
    void scan_recursive_findsSubPackage() {
        Set<Class<?>> classes = scanner.scan(PKG, true);

        assertTrue(classes.stream().anyMatch(c -> c.getSimpleName().equals("SubClass")));
    }

    @Test
    void scan_emptyPackage_returnsEmpty() {
        Set<Class<?>> classes = scanner.scan("io.github.PlatovD.svarog.nonexistent", false);

        assertNotNull(classes);
        assertTrue(classes.isEmpty());
    }

    @Test
    void scan_nullPackage_throws() {
        assertThrows(
                SvarogScannerException.class,
                () -> scanner.scan(null, false));
    }
}