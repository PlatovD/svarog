package io.github.PlatovD.svarog.scanner.annotated;

import io.github.PlatovD.svarog.annotation.AfterCreate;
import io.github.PlatovD.svarog.annotation.AutoCreate;

@AutoCreate
public class StaticLifecycleMethod {

    @AfterCreate
    public static void init() {
    }
}