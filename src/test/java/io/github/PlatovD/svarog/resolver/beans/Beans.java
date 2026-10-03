package io.github.PlatovD.svarog.resolver.beans;

public final class Beans {

    private Beans() {
    }

    public static class Repository {
    }

    public static class Service {
        Repository repository;
    }

    public static class Controller {
        Service service;
    }
}