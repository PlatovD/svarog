package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.config.factory.PropertiesFactory;
import io.github.PlatovD.svarog.exception.SvarogException;
import io.github.PlatovD.svarog.resolver.ResolutionPipeline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerBuilderTest {

    @Test
    void config_returnsSameBuilder() {
        ContainerBuilder builder = new ContainerBuilder();
        assertSame(builder, builder.config(new PropertiesFactory()));
    }

    @Test
    void pipeline_returnsSameBuilder() {
        ContainerBuilder builder = new ContainerBuilder();
        assertSame(builder, builder.pipeline(ResolutionPipeline.defaultPipeline()));
    }

    @Test
    void config_null_throws() {
        assertThrows(
                SvarogException.class,
                () -> new ContainerBuilder().config(null));
    }

    @Test
    void pipeline_null_throws() {
        assertThrows(
                SvarogException.class,
                () -> new ContainerBuilder().pipeline(null));
    }

    @Test
    void builder_starts_withDefaults() {
        assertDoesNotThrow(ContainerBuilder::new);
    }
}