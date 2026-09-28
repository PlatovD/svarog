package io.github.PlatovD.svarog.config.factory;

import io.github.PlatovD.svarog.config.reader.ConfigReader;

@FunctionalInterface
public interface ConfigFactory {

    ConfigReader createReader();
}