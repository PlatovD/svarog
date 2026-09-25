package io.github.PlatovD.svarog.util.config;

import java.util.Properties;

@FunctionalInterface
public interface PropertiesReader {

    Properties read();
}