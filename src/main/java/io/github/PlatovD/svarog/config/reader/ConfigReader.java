package io.github.PlatovD.svarog.config.reader;

import io.github.PlatovD.svarog.config.dto.ConfigDTO;

@FunctionalInterface
public interface ConfigReader {
    ConfigDTO read();
}
