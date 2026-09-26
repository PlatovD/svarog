package io.github.PlatovD.svarog.definition;

import java.util.List;

class SampleBeans {

    static class Repository {
    }

    static class Service {
        Repository repository;
        List<String> items;

        public void init() {
        }

        public void close() {
        }
    }
}
