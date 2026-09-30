package io.github.PlatovD.svarog.scanner.annotated;

import io.github.PlatovD.svarog.annotation.AfterCreate;
import io.github.PlatovD.svarog.annotation.AutoCreate;
import io.github.PlatovD.svarog.annotation.BeforeDestroy;

@AutoCreate
public class LifecycleService {

    @AfterCreate
    public void init() {
    }

    @BeforeDestroy
    public void close() {
    }
}