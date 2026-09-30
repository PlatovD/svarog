package io.github.PlatovD.svarog.scanner.annotated;

import io.github.PlatovD.svarog.annotation.AfterCreate;
import io.github.PlatovD.svarog.annotation.AutoCreate;

@AutoCreate
public class DuplicateAfterCreate {

    @AfterCreate
    public void first() {
    }

    @AfterCreate
    public void second() {
    }
}