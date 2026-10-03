package io.github.PlatovD.svarog.resolver.step;

import io.github.PlatovD.svarog.definition.BeanDefinition;
import io.github.PlatovD.svarog.definition.BeanDefinitionBuilder;
import io.github.PlatovD.svarog.exception.AmbiguousBeanException;
import io.github.PlatovD.svarog.resolver.ResolutionContext;
import io.github.PlatovD.svarog.resolver.beans.Beans;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndexingStepTest {

    private final IndexingStep step = new IndexingStep();

    private static BeanDefinition def(Class<?> type) {
        return new BeanDefinitionBuilder().type(type).build();
    }

    private static BeanDefinition named(Class<?> type, String name) {
        return new BeanDefinitionBuilder().type(type).name(name).build();
    }

    @Test
    void apply_singleBean_indexedByType() {
        BeanDefinition repo = def(Beans.Repository.class);

        ResolutionContext ctx = new ResolutionContext(List.of(repo));
        step.apply(ctx);

        assertEquals(1, ctx.getByType().get(Beans.Repository.class).size());
        assertSame(repo, ctx.getByType().get(Beans.Repository.class).get(0));
    }

    @Test
    void apply_multipleBeansSameType_allIndexed() {
        BeanDefinition a = named(Beans.Repository.class, "mainRepo");
        BeanDefinition b = named(Beans.Repository.class, "backupRepo");

        ResolutionContext ctx = new ResolutionContext(List.of(a, b));
        step.apply(ctx);

        assertEquals(2, ctx.getByType().get(Beans.Repository.class).size());
    }

    @Test
    void apply_explicitName_indexedByName() {
        BeanDefinition repo = named(Beans.Repository.class, "mainRepo");

        ResolutionContext ctx = new ResolutionContext(List.of(repo));
        step.apply(ctx);

        assertSame(repo, ctx.getByName().get("mainRepo"));
    }

    @Test
    void apply_defaultName_isFullClassName() {
        BeanDefinition repo = def(Beans.Repository.class);

        ResolutionContext ctx = new ResolutionContext(List.of(repo));
        step.apply(ctx);

        assertSame(repo, ctx.getByName().get(Beans.Repository.class.getName()));
    }

    @Test
    void apply_duplicateName_throws() {
        BeanDefinition a = named(Beans.Repository.class, "dup");
        BeanDefinition b = named(Beans.Repository.class, "dup");

        ResolutionContext ctx = new ResolutionContext(List.of(a, b));

        assertThrows(AmbiguousBeanException.class, () -> step.apply(ctx));
    }

    @Test
    void apply_emptyInput_producesEmptyIndexes() {
        ResolutionContext ctx = new ResolutionContext(List.of());
        step.apply(ctx);

        assertTrue(ctx.getByType().isEmpty());
        assertTrue(ctx.getByName().isEmpty());
    }
}