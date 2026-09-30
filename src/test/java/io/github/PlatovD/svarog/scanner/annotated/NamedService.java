package io.github.PlatovD.svarog.scanner.annotated;

import io.github.PlatovD.svarog.annotation.AutoCreate;
import io.github.PlatovD.svarog.definition.Scope;

@AutoCreate(name = "customName", scope = Scope.PROTOTYPE, lazy = true)
public class NamedService {
}