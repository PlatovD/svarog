package io.github.PlatovD.svarog.core.testbeans.container;

import io.github.PlatovD.svarog.annotation.AfterCreate;
import io.github.PlatovD.svarog.annotation.AutoCreate;
import io.github.PlatovD.svarog.annotation.AutoInject;
import io.github.PlatovD.svarog.annotation.BeforeDestroy;

@AutoCreate
public class Service {

    @AutoInject
    public Repository repository;

    public boolean initialized = false;
    public boolean closed = false;

    @AfterCreate
    public void afterCreate() {
        initialized = true;
    }

    @BeforeDestroy
    public void beforeDestroy() {
        closed = true;
    }
}
