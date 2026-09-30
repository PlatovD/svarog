package io.github.PlatovD.svarog.scanner.annotated;

import io.github.PlatovD.svarog.annotation.AutoCreate;
import io.github.PlatovD.svarog.annotation.AutoInject;

@AutoCreate
public class InjectedService {

    @AutoInject
    private SimpleService simpleService;

    @AutoInject(qualifier = "customName")
    private NamedService namedService;

    private String notInjected;
}