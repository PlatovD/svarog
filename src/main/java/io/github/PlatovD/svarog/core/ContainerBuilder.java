package io.github.PlatovD.svarog.core;

import io.github.PlatovD.svarog.config.factory.ConfigFactory;
import io.github.PlatovD.svarog.config.factory.PropertiesFactory;
import io.github.PlatovD.svarog.exception.SvarogException;
import io.github.PlatovD.svarog.resolver.ResolutionPipeline;

public final class ContainerBuilder {

    private ConfigFactory configFactory = new PropertiesFactory();
    private ResolutionPipeline pipeline = ResolutionPipeline.defaultPipeline();

    public ContainerBuilder config(ConfigFactory configFactory) {
        if (configFactory == null) {
            throw new SvarogException("configFactory must not be null");
        }
        this.configFactory = configFactory;
        return this;
    }

    public ContainerBuilder pipeline(ResolutionPipeline pipeline) {
        if (pipeline == null) {
            throw new SvarogException("pipeline must not be null");
        }
        this.pipeline = pipeline;
        return this;
    }

    public Container build() {
        return new Container(configFactory, pipeline);
    }
}